package com.example.projekt.controller;
import com.example.projekt.AppEventBus;
import com.example.projekt.WeekDay;
import com.example.projekt.event.ChangeViewEvent;
import com.example.projekt.event.bus.PlanSavedEvent;
import com.example.projekt.service.PlanService;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import java.io.IOException;
import java.util.*;

public class PlanCreatorController {
    @FXML
    private TextField planTitleField;
    @FXML
    private VBox workoutBox;
    @FXML
    private VBox exerciseBox;
    @FXML
    private VBox daysBox;

    private Map<WeekDay, WorkoutCreatorController> workoutControllerMap = new EnumMap<WeekDay, WorkoutCreatorController>(WeekDay.class);
    private WeekDay currentDay;
    private ExercisesListController exercisesListController;
    private final IntegerProperty selectedDayNumber = new SimpleIntegerProperty();

    private final PlanService planService = PlanService.getInstance();

    private void initializeDays(){
        Arrays.stream(WeekDay.values()).forEach(day -> {
            daysBox.getChildren().add(createDayPane(day));
        });
    }

    private void addExerciseList(){
        try {
            FXMLLoader exerciseLoader = new FXMLLoader(getClass().getResource("/view/exercises-list-view.fxml"));
            Node exerciseBoxLoaded = exerciseLoader.load();
            exerciseBoxLoaded.setVisible(false);
            exercisesListController = exerciseLoader.getController();
            exerciseBox.getChildren().add(exerciseBoxLoaded);

            exercisesListController.setOnExerciseSelected(exercise -> {
                workoutControllerMap.get(currentDay).addExercise(exercise);
            });

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
    }


    @FXML
    public void initialize() {
        planTitleField.getStyleClass().add("styled-text-field");

        initializeDays();
        addExerciseList();
    }

    private VBox createDayPane(WeekDay day) {
        Label titleLabel = new Label(day.getDisplayName());
        titleLabel.getStyleClass().add("day-title");
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        Button addButton = new Button("+ Add Workout");
        addButton.setUserData(day.getNumber());
        addButton.setOnAction(this::handleAddWorkout);
        addButton.setMaxWidth(Double.MAX_VALUE);
        addButton.getStyleClass().add("add-workout-button");

        Label statusLabel = new Label("No workout");
        statusLabel.getStyleClass().add("status-label");
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setMaxWidth(Double.MAX_VALUE);

        VBox container = new VBox();
        container.setSpacing(0);
        container.setAlignment(Pos.TOP_CENTER);
        container.getChildren().addAll(titleLabel, statusLabel, addButton);
        container.getStyleClass().add("day-card");

        selectedDayNumber.addListener((observable, oldValue, newValue) -> {
            if (day.getNumber() == newValue.intValue()) {
                container.getStyleClass().removeAll("day-card");
                container.getStyleClass().add("day-card-selected");

                titleLabel.getStyleClass().removeAll("day-title");
                titleLabel.getStyleClass().add("day-title-selected");

                if (workoutControllerMap.containsKey(day)) {
                    statusLabel.setText("Workout added ✓");
                    statusLabel.getStyleClass().removeAll("status-label", "status-label-prompt");
                    statusLabel.getStyleClass().add("status-label-added");
                } else {
                    statusLabel.setText("Click to add workout");
                    statusLabel.getStyleClass().removeAll("status-label", "status-label-added");
                    statusLabel.getStyleClass().add("status-label-prompt");
                }
            } else {
                container.getStyleClass().removeAll("day-card-selected");
                container.getStyleClass().add("day-card");

                titleLabel.getStyleClass().removeAll("day-title-selected");
                titleLabel.getStyleClass().add("day-title");

                if (workoutControllerMap.containsKey(day)) {
                    statusLabel.setText("Workout added ✓");
                    statusLabel.getStyleClass().removeAll("status-label", "status-label-prompt");
                    statusLabel.getStyleClass().add("status-label-added");
                } else {
                    statusLabel.setText("No workout");
                    statusLabel.getStyleClass().removeAll("status-label-added", "status-label-prompt");
                    statusLabel.getStyleClass().add("status-label");
                }
            }

            if (workoutControllerMap.containsKey(day)) {
                addButton.setText("Edit Workout");
            } else {
                addButton.setText("+ Add Workout");
            }
        });

        return container;
    }

    private void showErrorAlert(String messae) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("error");
        alert.setHeaderText(null);
        alert.setContentText(messae);
        alert.showAndWait();
    }

    @FXML
    private void handleSavePlan() {
        boolean isValid = workoutControllerMap.values().stream()
                        .allMatch(WorkoutCreatorController::validate);

        if(!isValid) {
            showErrorAlert("sets and reps values cannot be empty");
            return;
        }

        if(workoutControllerMap.isEmpty()) {
            showErrorAlert("add at lest one workout");
            return;
        }

        String title = planTitleField.getText();

        if(title.isEmpty()){
            showErrorAlert("title cannot be empty");
            return;
        }

        daysBox.fireEvent(new ChangeViewEvent("/view/plan-list-view.fxml"));

        Thread.startVirtualThread(() -> {
            try{
                planService.savePlan(workoutControllerMap, title);
                AppEventBus.getAsyncBus().post(new PlanSavedEvent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
        if(currentDay != null && workoutControllerMap.get(currentDay).isEmpty()){
            workoutControllerMap.remove(currentDay);
        }

        Button source = (Button) event.getSource();
        int dayNumber = Integer.parseInt(source.getUserData().toString());
        currentDay = WeekDay.fromNumber(dayNumber);

        WorkoutCreatorController currentDayController = workoutControllerMap.computeIfAbsent(currentDay, k -> addWorkoutToMap());
        displayWorkout(currentDayController);
        selectedDayNumber.set(dayNumber);
    }
}
