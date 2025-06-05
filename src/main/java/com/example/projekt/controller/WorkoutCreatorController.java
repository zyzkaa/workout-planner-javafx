package com.example.projekt.controller;

import com.example.projekt.component.ExerciseInput;
import com.example.projekt.model.dto.ExerciseDto;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WorkoutCreatorController {
    @FXML
    private VBox exercisesBox;
    @FXML
    private VBox root;
    @FXML
    private Text text;


    private String initialText;
    private final ObservableList<ExerciseDto> exercises = FXCollections.observableArrayList();

    public void setText(String text) {
        this.text.setText(text);
    }

    public WorkoutCreatorController(String text){
        initialText = text;
    }

    public Node getRoot(){
        return root;
    }

    public void addExercise(ExerciseDto newExercise){
        if(exercises.contains(newExercise)){
            return;
        }
        exercises.add(newExercise);
    }

    public List<ExerciseDto> getExercises(){
        return exercises;
    }

    public boolean validate(){
        List<Node> nodes = new ArrayList<>(exercisesBox.getChildren());
        return nodes.stream()
                        .map(exercise -> (ExerciseInput) exercise)
                        .allMatch(ExerciseInput::validate);
    }

    @Getter
    public class ExerciseWithData{
        ExerciseDto exercise;
        int sets;
        int reps;

        public ExerciseWithData(ExerciseDto exercise, int sets, int reps) {
            this.exercise = exercise;
            this.sets = sets;
            this.reps = reps;
        }
    }

    public List<ExerciseWithData> getExercisesWithData(){
        List<Node> nodes = exercisesBox.getChildren();
        return IntStream.range(0, exercises.size())
                .mapToObj(index -> {
                    ExerciseInput currentNode = (ExerciseInput) nodes.get(index);
                    return new ExerciseWithData(exercises.get(index), currentNode.getSets(), currentNode.getReps());
                })
                .collect(Collectors.toList());
    }

    public void initialize() {
        text.setText(initialText);

        exercises.addListener((ListChangeListener<ExerciseDto>) change -> {
            while(change.next()) {
                if(change.wasAdded()) {
                    List<? extends ExerciseDto> addedExercises = change.getAddedSubList();
                    if(addedExercises.size() > 1) {
                        throw new IllegalStateException("More than one exercise was added");
                    }
                    ExerciseDto exercise = addedExercises.getFirst();
                    ExerciseInput newExerciseInput = new ExerciseInput(exercise.getName());

                    newExerciseInput.setOnDelete(() -> exercises.remove(exercise));
                    exercisesBox.getChildren().add(newExerciseInput);
                }
                if(change.wasRemoved()) {
                    List<? extends ExerciseDto> removedExercises = change.getRemoved();
                    if(removedExercises.size() > 1) {
                        throw new IllegalStateException("More than one exercise was removed");
                    }
                    ExerciseDto removedExercise = removedExercises.getFirst();
                    exercisesBox.getChildren().removeIf(node ->
                            node instanceof ExerciseInput && ((ExerciseInput) node).getExerciseName().equals(removedExercise.getName())
                    );
                }
            }
        });
    }

    public boolean isEmpty() {
        return exercises.isEmpty();
    }
}
