package com.example.projekt.component;

import com.example.projekt.WeekDay;
import com.example.projekt.model.entity.Workout;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class OneDayDisplay extends VBox {

    private Label mainHeader;
    private VBox smallHeadersContainer;

    public OneDayDisplay(Workout workout) {
        mainHeader = new Label(workout.getDay().getDisplayName());
        smallHeadersContainer = new VBox();

        setupLayout();
        applyStyles();

        workout.getExercises().forEach(exercise -> {
            String text = exercise.getExercise().getName() + ": " + exercise.getSets() + " x " + exercise.getRepetitions();
            addSmallHeader(text);
        });
    }

    private void setupLayout() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(20));

        smallHeadersContainer.setAlignment(Pos.CENTER);
        smallHeadersContainer.setSpacing(8);
        smallHeadersContainer.setPadding(new Insets(10));

        this.getChildren().addAll(mainHeader, smallHeadersContainer);
    }

    private void applyStyles() {
        this.setStyle("-fx-background-color: white; " +
                "-fx-border-color: #e1e8ed; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2); " +
                "-fx-padding: 0 0 12 0;");

        mainHeader.setFont(Font.font("System", FontWeight.BOLD, 18));
        mainHeader.setStyle("-fx-text-fill: #2c3e50; " +
                "-fx-padding: 12 16 8 16; " +
                "-fx-alignment: center;");

        smallHeadersContainer.setStyle("-fx-padding: 0 16 4 16; " +
                "-fx-alignment: center;");
    }

    public void addSmallHeader(String text) {
        Label smallHeader = createSmallHeader(text);
        smallHeadersContainer.getChildren().add(smallHeader);
    }

    private Label createSmallHeader(String text) {
        Label smallHeader = new Label(text);
        smallHeader.setFont(Font.font("System", FontWeight.NORMAL, 16));
        smallHeader.setStyle("-fx-text-fill: #7f8c8d; " +
                "-fx-padding: 4 8 4 8; " +
                "-fx-alignment: center;");

        return smallHeader;
    }
}