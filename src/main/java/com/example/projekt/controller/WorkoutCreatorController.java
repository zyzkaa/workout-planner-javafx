package com.example.projekt.controller;

import com.example.projekt.component.ExerciseInput;
import com.example.projekt.model.dto.ExerciseDto;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class WorkoutCreatorController {
    @FXML
    private VBox exercisesBox;

    @FXML
    private Text text;
    private String initialText;

    public void setText(String text) {
        this.text.setText(text);
    }


    public WorkoutCreatorController() {}

    public WorkoutCreatorController(String text){
        System.out.println("create " + text);
        initialText = text;
    }

    public Node getRoot(){
        return exercisesBox;
    }

    public void addExercise(ExerciseDto newExercise){
        exercisesBox.getChildren().add(new ExerciseInput(newExercise.getName()));
    }

    public void initialize() {
        text.setText(initialText);
    }
}
