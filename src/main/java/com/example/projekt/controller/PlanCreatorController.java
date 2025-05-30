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
import java.util.Arrays;
import java.util.List;

public class PlanCreatorController {
    @FXML
    private TextField planTitleField;
    @FXML
    private VBox workoutBox;
    @FXML
    private VBox daysBox;
    @FXML
    private VBox exerciseBox;

    IntegerProperty selectedDayNumber = new SimpleIntegerProperty();


    @FXML
    public void initialize() {
        Arrays.stream(WeekDay.values()).forEach(day -> {
            daysBox.getChildren().add(createDayPane(day));
        });

        try {
            VBox exerciseList = FXMLLoader.load(getClass().getResource("/view/exercises-list-view.fxml"));
            exerciseList.setVisible(false);
            exerciseBox.getChildren().add(exerciseList);

            ListChangeListener<Node> listener = new ListChangeListener<>() {
                @Override
                public void onChanged(Change<? extends Node> change) {
                    while (change.next()) {
                        if (change.wasAdded()) {
                            exerciseList.setVisible(true);
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

    @FXML
    private void handleCancel() {
        planTitleField.fireEvent(new ChangeViewEvent("/view/plan-list-view.fxml"));
    }

    @FXML
    private void handleAddWorkout(javafx.event.ActionEvent event) {
        Button source = (Button) event.getSource();
        int dayNumber = Integer.parseInt(source.getUserData().toString());

        selectedDayNumber.set(dayNumber);

        workoutBox.getChildren().clear();
        try {
            Pane workoutPane = FXMLLoader.load(getClass().getResource("/view/workout-creator-view.fxml"));
            workoutBox.getChildren().add(workoutPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
