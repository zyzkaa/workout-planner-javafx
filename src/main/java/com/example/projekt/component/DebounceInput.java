package com.example.projekt.component;

import com.example.projekt.event.SearchEvent;
import javafx.animation.PauseTransition;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class DebounceInput extends TextField {
    private final Class<? extends SearchEvent> eventClass;
    private final PauseTransition debounce;
    private final double delay = 0.5;

    public DebounceInput(Class<? extends SearchEvent> eventClass) {
        this.eventClass = eventClass;
        debounce = new PauseTransition(Duration.seconds(delay));
        debounce.setOnFinished(finish -> fireEvent());

        textProperty().addListener((observable, oldValue, newValue) -> {
            debounce.stop();
            debounce.playFromStart();
        });
    }

    private void fireEvent() {
        try {
            SearchEvent instance = eventClass
                    .getDeclaredConstructor(String.class)
                    .newInstance(getText());
            this.fireEvent(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
