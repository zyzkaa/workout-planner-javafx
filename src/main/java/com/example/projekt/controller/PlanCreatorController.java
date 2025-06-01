package com.example.projekt.controller;
import com.example.projekt.WeekDay;
import com.example.projekt.event.ChangeViewEvent;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;

public class PlanCreatorController {
    @FXML
    private TextField planTitleField;
    @FXML
    private VBox workoutBox;
    private Map<WeekDay, WorkoutCreatorController> workoutControllerMap = new EnumMap<WeekDay, WorkoutCreatorController>(WeekDay.class);
    private WeekDay currentDay;
    @FXML
    private VBox daysBox;
    @FXML
    private VBox exerciseBox;
    private ExercisesListController exercisesListController;

    IntegerProperty selectedDayNumber = new SimpleIntegerProperty();


    @FXML
    public void initialize() {
        Arrays.stream(WeekDay.values()).forEach(day -> {
            daysBox.getChildren().add(createDayPane(day));
        });

        try {
            FXMLLoader exerciseLoader = new FXMLLoader(getClass().getResource("/view/exercises-list-view.fxml"));
            Node exerciseBoxLoaded = exerciseLoader.load();
            exerciseBoxLoaded.setVisible(false);
            exercisesListController = exerciseLoader.getController();
            exerciseBox.getChildren().add(exerciseBoxLoaded);

            ListChangeListener<Node> listener = new ListChangeListener<>() {
                @Override
                public void onChanged(Change<? extends Node> change) {
                    while (change.next()) {
                        if (change.wasAdded()) {
                            exerciseBoxLoaded.setVisible(true);
                            workoutBox.getChildren().removeListener(this);
                            break;
                        }
                    }
                }
            };

            workoutBox.getChildren().addListener(listener);

        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            FXMLLoader workoutLoader = new FXMLLoader(getClass().getResource("/view/workout-creator-view.fxml"));
//            Node workoutBoxLoaded = workoutLoader.load();
//            workoutCreatorController = workoutLoader.getController();
//            workoutBox.getChildren().add(workoutBoxLoaded);
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        exercisesListController.setOnExerciseSelected(exercise -> {
            workoutControllerMap.get(currentDay).addExercise(exercise);
        });
    }

    private VBox createDayPane(WeekDay day) {
        Label titleLabel = new Label(day.getDisplayName());
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #dddddd; -fx-padding: 5;");

        Button addButton = new Button("Add workout");
        addButton.setUserData(day.getNumber());
        addButton.setOnAction(this::handleAddWorkout);

        VBox container = new VBox(titleLabel, addButton);
        container.setSpacing(5);
        container.setPadding(new Insets(5));

        selectedDayNumber.addListener((observable, oldValue, newValue) -> {
            if(day.getNumber() == newValue.intValue()) {
                container.setStyle("-fx-background-color: #d766af;");
            } else {
                container.setStyle("");
            }
        });

        return container;
    }

    @FXML
    private void handleSavePlan() {
        String title = planTitleField.getText();
        System.out.println("Zapisuję plan: " + title);
        // dodaj walidację i zapis do bazy
    }


    private WorkoutCreatorController addWorkoutToMap(){
        FXMLLoader workoutLoader = new FXMLLoader(getClass().getResource("/view/workout-creator-view.fxml"));
        WorkoutCreatorController workoutController = new WorkoutCreatorController(currentDay.getDisplayName());
        workoutLoader.setController(workoutController);
        try {
            Node loaded = workoutLoader.load();
            return workoutController;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayWorkout(WorkoutCreatorController controller){
        try {
            workoutBox.getChildren().clear();
            workoutBox.getChildren().add(controller.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddWorkout(javafx.event.ActionEvent event) {
        Button source = (Button) event.getSource();
        int dayNumber = Integer.parseInt(source.getUserData().toString());
        currentDay = WeekDay.fromNumber(dayNumber);

        WorkoutCreatorController currentDayController = workoutControllerMap.computeIfAbsent(currentDay, k -> addWorkoutToMap());
        displayWorkout(currentDayController);
        selectedDayNumber.set(dayNumber);
    }

}
