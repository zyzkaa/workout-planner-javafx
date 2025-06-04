package com.example.projekt.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Plan {
    @Id
    @GeneratedValue
    private int id;

//    @OneToMany
//    private List<Client> clients = new ArrayList<>();

    private String title;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Workout> workouts;

    private Date added;
}
