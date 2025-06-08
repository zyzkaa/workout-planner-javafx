package com.example.projekt.event;

import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

public abstract class SearchEvent extends Event {
    @Getter
    private final String value;

    public SearchEvent(EventType<? extends Event> type, String value ) {
        super(type);
        this.value = value;
    }

}
