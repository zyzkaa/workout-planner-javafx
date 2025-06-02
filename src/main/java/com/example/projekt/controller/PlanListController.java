package com.example.projekt.controller;

import com.example.projekt.event.ChangeViewEvent;
import com.example.projekt.model.entity.Plan;
import com.example.projekt.service.PlanService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


import java.util.ArrayList;
import java.util.List;

public class PlanListController {
    @FXML
    VBox workoutList;

    List<Plan> plans = new ArrayList<>();

    @FXML
    public void initialize() {
        getAllPlans();
    }

    private HBox getRow(){
        HBox row = new HBox(10);
        row.setSpacing(20);
        row.setPadding(new Insets(15));
        row.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(row, Priority.ALWAYS);
        row.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 4, 0, 0, 2);"
        );
        return row;
    }

    private Button getButton(){
        Button button = new Button("Show");
        button.getStyleClass().add("add-workout-button");
        button.setOnAction(this::handleShowWorkout);
        return button;
    }

    private void handleShowWorkout(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        Integer planId = (Integer) button.getUserData();
        System.out.println(planId);
    }

    private void showAllPlans(List<Plan> planList) {
        planList.forEach(plan -> {
            HBox row = getRow();

            Label label = new Label(plan.getTitle());
            label.setStyle("-fx-font-size: 16px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");

            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);

            Button button = getButton();
            button.setUserData(plan.getId());

            HBox.setHgrow(label, Priority.ALWAYS);
            row.getChildren().addAll(label, region, button);
            workoutList.getChildren().add(row);
        });
    }

    public void getAllPlans() {
        Thread.startVirtualThread(() -> {
            try{
                plans = PlanService.getInstance().getAll();
                Platform.runLater(() -> showAllPlans(plans));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public void addWorkout() {
        workoutList.fireEvent(new ChangeViewEvent("/view/plan-creator-view.fxml")); // odpalaj z innego elementu
    }
}
