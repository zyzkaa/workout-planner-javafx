package com.example.projekt.event;

import javafx.event.Event;
import javafx.event.EventType;

public class ChangeViewEvent extends Event {
    public static final EventType<ChangeViewEvent> CHANGE_VIEW = new EventType<>(Event.ANY, "CHANGE_VIEW");
    private final String fxmlPath;

    public ChangeViewEvent(String fxmlPath) {
        super(CHANGE_VIEW);
        this.fxmlPath = fxmlPath;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }
}
