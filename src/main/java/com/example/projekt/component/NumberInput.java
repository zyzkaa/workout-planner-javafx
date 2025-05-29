package com.example.projekt.component;


import javafx.scene.control.TextField;

public class NumberInput extends TextField {
    @Override
    public void replaceText(int start, int end, String text) {
        if (text.matches("[0-9]*")) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (text.matches("[0-9]*")) {
            super.replaceSelection(text);
        }
    }

    public NumberInput() {
    }

    public NumberInput(int min, int max) {
        textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()) return;

            if(newValue.matches("\\d*")) {
                int val = getValue();
                if(val < min || val > max) {
                    setText(oldValue);
                }
            }
        });
    }

    public int getValue(){
        return Integer.parseInt(getText());
    }
}
