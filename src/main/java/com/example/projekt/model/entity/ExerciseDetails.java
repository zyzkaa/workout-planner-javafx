package com.example.projekt.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ExerciseDetails {
    @GeneratedValue
    @Id
    private Long id;

    private int sets;
    private int repetitions;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;

    @ManyToOne
    private Exercise exercise;

}
