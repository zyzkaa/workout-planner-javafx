package com.example.projekt.controller;

import com.example.projekt.api.ExerciseService;
import com.example.projekt.component.DebounceInput;
import com.example.projekt.event.ExerciseSearchEvent;
import com.example.projekt.model.dto.*;
import com.google.common.base.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import retrofit2.Response;
import javafx.application.Platform;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

// wywal empty choice?
// przy wyszukiwarce, po sekundzie rzuca zdarzenie do wyslania requesta
public class ExercisesListController {
    private final ObservableList<ExerciseDto> exercises = FXCollections.observableArrayList();
    List<BodyPartsDto> bodyParts;
    String emptyChoice = "-";

    @FXML
    private ComboBox<String> bodyPartsComboBox;

    @FXML
    private ListView<String> exercisesListView;

    @FXML
    private Button nextButton, prevButton;

    @FXML
    private HBox inputBox;

    private DebounceInput exerciseInput;

    private final int limit = 10;
    private int offset = 0;
    private int maxOffset = 10;
    private String currentBodyPart = "";
    private Supplier<WorkoutResponse<ExercisePage>> currentSearchFunc;

    private final int ROW_HEIGHT = 30;
    private String currentSearchString = "";

    private Consumer<ExerciseDto> onExerciseSelected;

    public void setOnExerciseSelected(Consumer<ExerciseDto> callback) {
        this.onExerciseSelected = callback;
    }

    public void initialize() {
        fetchBodyParts();

        exerciseInput = new DebounceInput(ExerciseSearchEvent.class);
        inputBox.getChildren().add(exerciseInput);
        inputBox.addEventHandler(ExerciseSearchEvent.eventType, event -> {
            bodyPartsComboBox.setValue(emptyChoice);
            currentSearchString = event.getValue();
            currentSearchFunc = this::fetchExercisesBySearch;
            reloadExercises();
        });

        exercisesListView.setFixedCellSize(ROW_HEIGHT);
        exercisesListView.setPrefHeight((limit + 1) * ROW_HEIGHT);
        exercisesListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                int index = exercisesListView.getSelectionModel().getSelectedIndex();
                System.out.println(exercises.get(index).getName());
                onExerciseSelected.accept(exercises.get(index));
            }
        });

        bodyPartsComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(emptyChoice)) return;

            exerciseInput.clear();
            currentBodyPart = newValue;
            currentSearchFunc = this::fetchExercisesByBodyPart;
            reloadExercises();
        });

        exercises.addListener((ListChangeListener<ExerciseDto>) change -> {
            Platform.runLater(() -> {
                exercisesListView.getItems().clear();
                exercisesListView.getItems().addAll(
                        exercises.stream().map(ExerciseDto::getName).limit(10).toList()
                );
            });
        });

        nextButton.setDisable(true);
        prevButton.setDisable(true);

        nextButton.setOnAction(event -> {
            if(currentSearchFunc == null) return;
            if(offset >= maxOffset) return;
            offset++;
            reloadExercises();
        });

        prevButton.setOnAction(event -> {
            if(currentSearchFunc == null) return;
            if(offset <= 0) return;
            offset--;
            reloadExercises();
        });
    }

    private void reloadExercises() {
        exercisesListView.getItems().clear();
        disableButtons();
        CompletableFuture
                .supplyAsync(currentSearchFunc)
                .thenAccept(fetched -> {
                    maxOffset = fetched.getData().getTotalPages() - 1;
                    System.out.println("total pages = " + maxOffset);
                    Platform.runLater(() -> {
                        exercises.setAll(fetched.getData().getExercises());
                    });
                    enableButtons();
                });
    }

    private void enableNextPrevButtons(){
        prevButton.setDisable(offset <= 0);
        nextButton.setDisable(offset >= maxOffset);
    }

    private void disableButtons(){
        prevButton.setDisable(true);
        nextButton.setDisable(true);
        bodyPartsComboBox.setDisable(true);
        exerciseInput.setDisable(true);
    }

    private void enableButtons(){
        enableNextPrevButtons();
        bodyPartsComboBox.setDisable(false);
        exerciseInput.setDisable(false);
    }

    private void fetchBodyParts() {
        Thread.startVirtualThread(() -> {
            try{
                Response<WorkoutResponse<List<BodyPartsDto>>> response = ExerciseService.getInstance().getExerciseApi().getBodyParts().execute();
                if (response.isSuccessful()) {
                    bodyParts = response.body().getData()
                            .stream()
                            .filter(bp -> !bp.getName().equals("cardio"))
                            .toList();

                    Platform.runLater(() -> {
                        bodyPartsComboBox.getItems().clear();
                        bodyParts.forEach(bp -> bodyPartsComboBox.getItems().add(bp.getName()));
                        bodyPartsComboBox.getItems().add(emptyChoice);
                        bodyPartsComboBox.getSelectionModel().select(emptyChoice);
                    });
                } else {
                    System.out.println("Error: " + response.errorBody().string());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

//    private void changeExerciseList(List<ExerciseDto> fetched) {
//        Platform.runLater(() -> {
//            exercises.setAll(fetched);
//        });
//    }

    private WorkoutResponse<ExercisePage> fetchExercisesByBodyPart() {
        if(currentBodyPart.equals(emptyChoice)) return null;
        try {
            Response<WorkoutResponse<ExercisePage>> response = ExerciseService.getInstance().getExerciseApi().getExercisesByBodyPart(currentBodyPart, limit, offset * limit).execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                System.out.println("Error: " + response.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private WorkoutResponse<ExercisePage> fetchExercisesBySearch() {
        try {
            Response<WorkoutResponse<ExercisePage>> response = ExerciseService.getInstance().getExerciseApi().getExercisesBySearch(currentSearchString, limit, offset * limit).execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                System.out.println("Error: " + response.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    private void fetchExercisesByBodyPart() {
//        Thread.startVirtualThread(() -> {
//            try{
//                Response<WorkoutResponse<ExercisePage>> response = ExerciseService.getInstance().getExerciseApi().getExercisesByBodyPart(currentBodyPart, limit, offset * limit).execute();
//                if (response.isSuccessful()) {
//                    changeExerciseList(response.body().getData().getExercises());
//                } else {
//                    System.out.println("Error: " + response.errorBody().string());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                Platform.runLater(() -> bodyPartsComboBox.setDisable(false));
//            }
//        });
//    }
//
//    private void fetchAllExercises() {
//        Thread.startVirtualThread(() -> {
//            try{
//                Response<WorkoutResponse<ExercisePage>> response = ExerciseService.getInstance().getExerciseApi().getExercises(limit).execute();
//
//                if (response.isSuccessful()) {
//                    exercises = response.body().getData().getExercises();
//                    exercises.forEach(e -> System.out.println(e.getName()));
//                } else {
//                    System.out.println("Error: " + response.errorBody().string());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
}
