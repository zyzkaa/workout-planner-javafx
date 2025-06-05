package com.example.projekt.controller;

import com.example.projekt.api.ExerciseService;
import com.example.projekt.component.DebounceInput;
import com.example.projekt.event.ExerciseSearchEvent;
import com.example.projekt.model.dto.*;
import com.example.projekt.service.ExercisesService;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.NoArgsConstructor;
import retrofit2.Response;
import javafx.application.Platform;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@NoArgsConstructor
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
    private Supplier<ExercisePage> currentSearchFunc;

    private final int ROW_HEIGHT = 30;
    private String currentSearchString = "";

    private Consumer<ExerciseDto> onExerciseSelected;

    public void setOnExerciseSelected(Consumer<ExerciseDto> callback) {
        this.onExerciseSelected = callback;
    }

    public void initialize() {
        exerciseInput = new DebounceInput(ExerciseSearchEvent.class);
        VBox.setVgrow(exerciseInput, Priority.ALWAYS);
        HBox.setHgrow(exerciseInput, Priority.ALWAYS);

        inputBox.getChildren().add(exerciseInput);
        inputBox.addEventHandler(ExerciseSearchEvent.eventType, event -> {
            currentSearchString = event.getValue();

            if(currentSearchString.isEmpty()) return;

            bodyPartsComboBox.setValue(emptyChoice);
            currentSearchFunc = this::fetchExercisesBySearch;
            reloadExercises();
        });

        exercisesListView.setFixedCellSize(ROW_HEIGHT);
        exercisesListView.setPrefHeight(limit * (ROW_HEIGHT + 0.5));
        exercisesListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                int index = exercisesListView.getSelectionModel().getSelectedIndex();
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

        disableButtons();
        fetchBodyParts();
    }

    private void reloadExercises() {
        exercisesListView.getItems().clear();
        disableButtons();
        CompletableFuture
                .supplyAsync(currentSearchFunc)
                .thenAccept(fetched -> {
                    maxOffset = fetched.getTotalPages() - 1;
                    Platform.runLater(() -> {
                        exercises.setAll(fetched.getExercises());
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
            List<BodyPartsDto> bodyParts = ExercisesService.getINSTANCE().fetchBodyParts();

            Platform.runLater(() -> {
                bodyPartsComboBox.getItems().clear();
                bodyParts.forEach(bp -> bodyPartsComboBox.getItems().add(bp.getName()));
                bodyPartsComboBox.getItems().add(emptyChoice);
                bodyPartsComboBox.getSelectionModel().select(emptyChoice);
                enableButtons();
            });
        });
    }

    private ExercisePage fetchExercisesByBodyPart() {
        if(currentBodyPart.equals(emptyChoice)) return null;
        return ExercisesService.getINSTANCE().fetchExercisesByBodyPart(currentBodyPart, limit, offset);
    }

    private ExercisePage fetchExercisesBySearch() {
        return ExercisesService.getINSTANCE().fetchExercisesBySearch(currentSearchString, limit, offset);
    }
}
