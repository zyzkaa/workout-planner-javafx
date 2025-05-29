package com.example.projekt.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Plan {
    @Id
    @GeneratedValue
    private int id;

    @OneToMany
    private List<Client> clients;

    private String title;

    @OneToMany
    private List<Workout> workouts;
}
