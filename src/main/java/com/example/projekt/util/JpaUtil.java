package com.example.projekt.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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

    public static void doInTransaction(Consumer<EntityManager> action) {
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
}
