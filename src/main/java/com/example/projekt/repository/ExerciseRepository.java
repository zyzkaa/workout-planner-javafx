package com.example.projekt.repository;

import com.example.projekt.model.entity.Exercise;
import com.example.projekt.util.JpaUtil;
import lombok.Getter;

import java.util.List;

public class ExerciseRepository {
    @Getter
    private final static ExerciseRepository INSTANCE = new ExerciseRepository();

    public Exercise findById(String exerciseId) {
        List<Exercise> list =  JpaUtil.doInTransaction(session -> session.createQuery("from Exercise where id = :id", Exercise.class).setParameter("id", exerciseId).getResultList());
        if (list.isEmpty()) return null;
        return list.getFirst();
    }

    public void add(Exercise exercise) {
        JpaUtil.doInTransactionVoid(session -> session.persist(exercise));
    }
}
