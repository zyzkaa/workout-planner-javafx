package com.example.projekt.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
public class Plan {
    @Id
    @GeneratedValue
    private int id;

    private String title;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Workout> workouts;

    private Date added;
}
