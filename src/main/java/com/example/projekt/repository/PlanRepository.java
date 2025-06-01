package com.example.projekt.repository;

import com.example.projekt.model.entity.Plan;
import com.example.projekt.util.JpaUtil;
import lombok.Getter;

import java.util.List;

public class PlanRepository {
    @Getter
    private final static PlanRepository INSTANCE = new PlanRepository();

    public void add(Plan plan) {
        JpaUtil.doInTransactionVoid(session -> session.persist(plan));
    }

    public List<Plan> findAll() {
        return JpaUtil.doInTransaction(session -> session.createQuery("from Plan", Plan.class).getResultList());
    }
}
