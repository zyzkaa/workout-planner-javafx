package com.example.projekt.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WorkoutResponse<T> {
    private boolean success;
    private T data;
}
