package com.example.projekt.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class JpaUtil {
    private static final EntityManagerFactory emf = buildEntityManagerFactory();

    private static EntityManagerFactory buildEntityManagerFactory() {
        Map<String, Object> settings = new HashMap<>();

        settings.put("jakarta.persistence.jdbc.driver", AppConfig.getProperty("jdbc.driver"));
        settings.put("jakarta.persistence.jdbc.url", AppConfig.getProperty("jdbc.url"));
        settings.put("hibernate.dialect", AppConfig.getProperty("hibernate.dialect"));
        settings.put("hibernate.hbm2ddl.auto", AppConfig.getProperty("hibernate.hbm2ddl.auto"));
        settings.put("hibernate.show_sql", AppConfig.getProperty("hibernate.show_sql"));
        settings.put("hibernate.format_sql", AppConfig.getProperty("hibernate.format_sql"));

        return new Persistence().createEntityManagerFactory("database", settings);
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void doInTransactionVoid(Consumer<EntityManager> action) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            action.accept(em);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    public static <T> T doInTransaction(Function<EntityManager, T> action) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            T result = action.apply(em);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }
}
