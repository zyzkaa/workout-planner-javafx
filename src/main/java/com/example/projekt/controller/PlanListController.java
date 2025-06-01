package com.example.projekt.controller;

import com.example.projekt.event.ChangeViewEvent;
import com.example.projekt.model.entity.Plan;
import com.example.projekt.service.PlanService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.util.List;

public class PlanListController {
    @FXML
    VBox workoutList;

    @FXML
    public void initialize() {
        getAllPlans();
    }

    private void showAllPlans(List<Plan> planList) {
        planList.forEach(plan -> {
            HBox row = new HBox(10);
            row.setPadding(new Insets(5, 0,0,0));

            Label label = new Label(plan.getTitle());
            Button editButton = new Button("Edytuj");

            row.getChildren().addAll(label, editButton);
            workoutList.getChildren().add(row);
        });
    }

    public void getAllPlans() {
        Thread.startVirtualThread(() -> {
            try{
                List<Plan> plans = PlanService.getInstance().getAll();
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
