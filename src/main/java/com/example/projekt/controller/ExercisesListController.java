package com.example.projekt.controller;

import com.example.projekt.api.ExerciseService;
import com.example.projekt.model.dto.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import okhttp3.ResponseBody;
import retrofit2.Response;
import javafx.application.Platform;


import java.util.List;

// policz max offset
// przy wyszukiwarce, po sekundzie rzuca zdarzenie do wyslania requesta
public class ExercisesListController {
    private final ObservableList<ExerciseDto> exercises = FXCollections.observableArrayList();
    List<BodyPartsDto> bodyParts;
    String emptyChoice = "-";

    @FXML
    private ComboBox<String> bodyPartsComboBox;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> exercisesListView;

    @FXML
    private Button nextButton, prevButton;

    private final int limit = 10;
    private int offset = 0;
    private int maxOffset = 10;
    private String currentBodyPart = "";
    private Runnable currentSearch;

    public void initialize() {
        fetchBodyParts();

        bodyPartsComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(emptyChoice)){
                return;
            }

            bodyPartsComboBox.setDisable(true);
            currentBodyPart = newValue.toString();
            currentSearch = this::fetchExercisesByBodyPart;
            fetchExercisesByBodyPart();
        });

        exercises.addListener((ListChangeListener<ExerciseDto>) change -> {
            Platform.runLater(() -> {
                exercisesListView.getItems().clear();
                exercisesListView.getItems().addAll(
                        exercises.stream().map(ExerciseDto::getName).toList()
                );
            });
        });


        nextButton.setOnAction(event -> {
            if(currentSearch == null) return;
            if(offset >= maxOffset) return;
            offset++;
            currentSearch.run();
        });

        prevButton.setOnAction(event -> {
            if(currentSearch == null) return;
            if(offset <= 0) return;
            offset--;
            currentSearch.run();
        });
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

    private void changeExerciseList(List<ExerciseDto> fetched) {
        Platform.runLater(() -> {
            exercises.setAll(fetched);
        });
    }

    private void fetchExercisesByBodyPart() {
        Thread.startVirtualThread(() -> {
            try{
                Response<WorkoutResponse<ExercisePage>> response = ExerciseService.getInstance().getExerciseApi().getExercisesByBodyPart(currentBodyPart, limit, offset * limit).execute();
                if (response.isSuccessful()) {
                    changeExerciseList(response.body().getData().getExercises());
                } else {
                    System.out.println("Error: " + response.errorBody().string());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Platform.runLater(() -> bodyPartsComboBox.setDisable(false));
            }
        });
    }
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
