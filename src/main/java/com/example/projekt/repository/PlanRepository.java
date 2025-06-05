package com.example.projekt.repository;

import com.example.projekt.model.entity.Plan;
import com.example.projekt.util.JpaUtil;
import lombok.Getter;
import org.hibernate.annotations.NamedQuery;

import java.util.List;

public class PlanRepository {
    @Getter
    private final static PlanRepository INSTANCE = new PlanRepository();

    public void add(Plan plan) {
        JpaUtil.doInTransactionVoid(session -> session.persist(plan));
    }

    public List<Plan> findAll() {
        return JpaUtil.doInTransaction(session -> session.createQuery("from Plan order by added desc", Plan.class).getResultList());
    }

    public Plan findById(int id) {
        return JpaUtil.doInTransaction(session ->
                session.createQuery("select p from Plan p left join fetch p.workouts w left join fetch w.exercises e left join fetch e.exercise where p.id = :id", Plan.class)
                        .setParameter("id", id).getSingleResult());
    }

    public void deleteById(int id) {
        JpaUtil.doInTransactionVoid(session -> session.createQuery("delete from Plan p where p.id = :id").setParameter("id", id).executeUpdate());
    }
}
