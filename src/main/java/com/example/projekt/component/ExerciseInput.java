package com.example.projekt.component;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ExerciseInput extends HBox {
    private final Text name;
    //private final ImageView imageView;
    private final NumberInput setNumber;
    private final NumberInput repsNumber;

    public ExerciseInput(String name) {
        this.name = new Text(name);
        //this.imageView = new ImageView(image);
        this.setNumber = new NumberInput(1,100);
        this.repsNumber = new NumberInput(1,100);

//        imageView.setFitWidth(100);
//        imageView.setFitHeight(100);
//        imageView.setPreserveRatio(true);

        VBox rightBox = new VBox(10);
        HBox inputsBox = new HBox(5);

        setNumber.setPromptText("Sets");
        repsNumber.setPromptText("Reps");
        setNumber.setPrefWidth(60);
        repsNumber.setPrefWidth(60);

        inputsBox.getChildren().addAll(setNumber, repsNumber);
        rightBox.getChildren().addAll(this.name, inputsBox);

        this.setSpacing(20);
        this.getChildren().addAll(rightBox);
    }


}
