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

import java.util.List;

public class WorkoutCreatorController {
    @FXML
    private VBox exercisesBox;

    @FXML
    private Text text;
    private String initialText;

    private final ObservableList<ExerciseDto> exercises = FXCollections.observableArrayList();

    public void setText(String text) {
        this.text.setText(text);
    }

    public WorkoutCreatorController() {}

    public WorkoutCreatorController(String text){
        initialText = text;
    }

    public Node getRoot(){
        return exercisesBox;
    }

    public void addExercise(ExerciseDto newExercise){
        exercises.add(newExercise);
//        exercisesBox.getChildren().add(new ExerciseInput(newExercise.getName()));
    }

    public List<ExerciseDto> getExercises(){
        return exercises;
//        return exercises.stream().toList();
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
                    exercisesBox.getChildren().add(
                            new ExerciseInput(addedExercises.getFirst().getName())
                    );
                }
            }
        });
    }
}
