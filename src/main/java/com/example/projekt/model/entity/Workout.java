package com.example.projekt.model.entity;

import com.example.projekt.WeekDay;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Workout {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Plan plan;

    @OneToMany
    @Cascade({CascadeType.ALL})
    private List<ExerciseDetails> exercises;

    @Enumerated(EnumType.STRING)
    private WeekDay day;
}
