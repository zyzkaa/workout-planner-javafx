package com.example.projekt.controller;

import com.example.projekt.AppEventBus;
import com.example.projekt.component.OneDayDisplay;
import com.example.projekt.event.ChangeViewEvent;
import com.example.projekt.event.bus.PlanSavedEvent;
import com.example.projekt.model.entity.Plan;
import com.example.projekt.model.entity.Workout;
import com.example.projekt.service.PlanService;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PlanListController {
    @FXML
    VBox planList;

    List<Plan> plans = new ArrayList<>();

    @FXML
    VBox planDisplay;

    @FXML
    public void initialize() {
        getAllPlans();
        AppEventBus.getAsyncBus().register(this);
    }

    @Subscribe
    private void onPlanSaved(PlanSavedEvent event) {
        System.out.println("Plan Saved event caught");
        getAllPlans();
    }

    private HBox getRow(){
        HBox row = new HBox(10);
        row.setSpacing(20);
        row.setPadding(new Insets(15));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPrefHeight(60);
        row.setMinHeight(60);
        row.setMaxHeight(60);
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

    private Button getShowButton(){
        Button button = new Button("Show");
        button.getStyleClass().add("add-workout-button");
        button.setOnAction(this::handleShowPlan);
        return button;
    }

    private Button getDeleteButton(){
        Button button = new Button("Delete");
        button.getStyleClass().add("delete-workout-button");
        button.setOnAction(this::handleDeletePlan);
        return button;
    }

    private Label getPlanNameLabel(String name){
        Label label = new Label(name);
        label.setFont(Font.font("System", FontWeight.BOLD, 28));
        label.setStyle("-fx-text-fill: #2c3e50; " +
                "-fx-padding: 12 16 8 16; " +
                "-fx-alignment: center;");
        return label;
    }

    private void displayPlan(Plan plan){
        planDisplay.getChildren().add(getPlanNameLabel(plan.getTitle()));
        plan.getWorkouts()
                .stream()
                .sorted(Comparator.comparing(workout -> workout.getDay().getNumber()))
                .forEach(workout -> planDisplay.getChildren().add(new OneDayDisplay(workout)));
    }

    private boolean showDeleteConfirmation(String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting workout");
        alert.setHeaderText("Are you sure you want to delete workout: " + title);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void handleDeletePlan(ActionEvent actionEvent){
        Button button = (Button) actionEvent.getSource();
        Integer planId = (Integer) button.getUserData();

        HBox row = (HBox) button.getParent();
        Label label = (Label) row.getChildren().getFirst();
        if(!showDeleteConfirmation(label.getText())) return;

        Thread.startVirtualThread(() -> {
            try {
                PlanService.getInstance().deleteById(planId);
                getAllPlans();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void handleShowPlan(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        Integer planId = (Integer) button.getUserData();
        planDisplay.getChildren().clear();

        Thread.startVirtualThread(() -> {
            try {
                Plan plan = PlanService.getInstance().getById(planId);
                Platform.runLater(() -> displayPlan(plan));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showAllPlans(List<Plan> planList) {
        planList.forEach(plan -> {
            HBox row = getRow();

            Label label = new Label(plan.getTitle());
            label.setStyle("-fx-font-size: 16px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");

            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);

            Button showButton = getShowButton();
            showButton.setUserData(plan.getId());

            Button deleteButton = getDeleteButton();
            deleteButton.setUserData(plan.getId());

            HBox.setHgrow(label, Priority.ALWAYS);
            row.getChildren().addAll(label, region, deleteButton, showButton);
            this.planList.getChildren().add(row);
        });
    }

    private void getAllPlans() {
        Platform.runLater(() -> planList.getChildren().clear());
        Thread.startVirtualThread(() -> {
            try{
                plans = PlanService.getInstance().getAll();
                Platform.runLater(() -> showAllPlans(plans));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleAddPlan() {
        planList.fireEvent(new ChangeViewEvent("/view/plan-creator-view.fxml")); // odpalaj z innego elementu
    }
}
