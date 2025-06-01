package com.example.projekt.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Plan {
    @Id
    @GeneratedValue
    private int id;

    @OneToMany
    private List<Client> clients = new ArrayList<>();

    private String title;

    @OneToMany
    @Cascade(CascadeType.ALL)
    private List<Workout> workouts;
}
