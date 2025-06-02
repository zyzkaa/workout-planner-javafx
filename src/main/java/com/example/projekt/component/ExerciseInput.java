package com.example.projekt.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ExerciseInput extends HBox {
    private final Text name;
    private final NumberInput setNumber;
    private final NumberInput repsNumber;
    private final Button deleteButton;
    private Runnable onDelete; // Callback for delete action

    public ExerciseInput(String name) {
        this.name = new Text(name);
        this.setNumber = new NumberInput(1, 100);
        this.repsNumber = new NumberInput(1, 100);
        this.deleteButton = new Button("×");

        setupStyling();
        setupLayout();
    }

    private void setupStyling() {
        name.setFont(Font.font("System", FontWeight.BOLD, 14));
        name.setStyle("-fx-fill: #2c3e50;");

        setNumber.setPromptText("Sets");
        repsNumber.setPromptText("Reps");
        setNumber.setPrefWidth(70);
        repsNumber.setPrefWidth(70);

        String inputStyle = "-fx-background-color: #f8f9fa; " +
                "-fx-border-color: #dee2e6; " +
                "-fx-border-radius: 4; " +
                "-fx-background-radius: 4; " +
                "-fx-padding: 8;";
        setNumber.setStyle(inputStyle);
        repsNumber.setStyle(inputStyle);

        deleteButton.setPrefSize(30, 30);
        deleteButton.setStyle(
                "-fx-background-color: #e74c3c; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-radius: 15; " +
                        "-fx-cursor: hand;"
        );

        this.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: #e9ecef; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 8; " +
                        "-fx-background-radius: 8; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"
        );
        this.setPadding(new Insets(15));
    }

    private void setupLayout() {
        VBox contentBox = new VBox(8);
        contentBox.setAlignment(Pos.CENTER_LEFT);

        HBox inputsBox = new HBox(10);
        inputsBox.setAlignment(Pos.CENTER_LEFT);

        VBox setsContainer = new VBox(4);
        Text setsLabel = new Text("Sets");
        setsLabel.setStyle("-fx-fill: #6c757d; -fx-font-size: 12px;");
        setsContainer.getChildren().addAll(setsLabel, setNumber);

        VBox repsContainer = new VBox(4);
        Text repsLabel = new Text("Reps");
        repsLabel.setStyle("-fx-fill: #6c757d; -fx-font-size: 12px;");
        repsContainer.getChildren().addAll(repsLabel, repsNumber);

        inputsBox.getChildren().addAll(setsContainer, repsContainer);
        contentBox.getChildren().addAll(name, inputsBox);

        VBox deleteContainer = new VBox();
        deleteContainer.setAlignment(Pos.TOP_RIGHT);
        deleteContainer.getChildren().add(deleteButton);

        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(15);
        HBox.setHgrow(contentBox, Priority.ALWAYS);

        this.getChildren().addAll(contentBox, deleteContainer);

        deleteButton.setOnAction(e -> {
            if (onDelete != null) {
                onDelete.run();
            }
        });
    }

    public boolean validate() {
        return !setNumber.getText().isEmpty() && !repsNumber.getText().isEmpty();
    }

    public int getSets() {
        return Integer.parseInt(setNumber.getText());
    }

    public int getReps() {
        return Integer.parseInt(repsNumber.getText());
    }

    public String getExerciseName() {
        return name.getText();
    }

    public void setOnDelete(Runnable onDelete) {
        this.onDelete = onDelete;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }
}