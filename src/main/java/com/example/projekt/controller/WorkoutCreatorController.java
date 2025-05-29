package com.example.projekt.controller;

import com.example.projekt.component.ExerciseInput;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class WorkoutCreatorController {
    @FXML
    private VBox exercisesBox;
    @FXML
    private VBox searchBox;


    public void initialize() {
        addExercise();
    }

    private void addExercise() {
        exercisesBox.getChildren().add(new ExerciseInput("asd"));
    }
}
