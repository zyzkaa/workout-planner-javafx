    package com.example.projekt.event;

    import javafx.event.Event;
    import javafx.event.EventType;

    public class ExerciseSearchEvent extends SearchEvent {
        public static final EventType<ExerciseSearchEvent> eventType = new EventType<>(Event.ANY, "SEARCH_EXERCISES");

        public ExerciseSearchEvent(String value) {
            super(ExerciseSearchEvent.eventType, value);
        }
    }
