package com.example.projekt.util;

import com.example.projekt.model.entity.Exercise;
import com.example.projekt.model.entity.ExerciseDetails;
import com.example.projekt.model.entity.Plan;
import com.example.projekt.model.entity.Workout;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    static {
        sessionFactory = buildSessionFactory();
    }

    private static SessionFactory buildSessionFactory() {
        try {
            var config = new Configuration();
            System.out.println("Dialect loaded: " + AppConfig.getProperty("hibernate.dialect"));

            config.setProperty("hibernate.connection.driver_class", AppConfig.getProperty("jdbc.driver"));
            config.setProperty("hibernate.connection.url", AppConfig.getProperty("jdbc.url"));
            config.setProperty("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
            config.setProperty("hibernate.hbm2ddl.auto", AppConfig.getProperty("hibernate.hbm2ddl.auto"));
            config.setProperty("hibernate.show_sql", AppConfig.getProperty("hibernate.show_sql"));
            config.setProperty("hibernate.format_sql", AppConfig.getProperty("hibernate.format_sql"));

            config.addAnnotatedClass(Plan.class);
            config.addAnnotatedClass(Exercise.class);
            config.addAnnotatedClass(ExerciseDetails.class);
            config.addAnnotatedClass(Workout.class);


            var serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(config.getProperties())
                    .build();

            return config.buildSessionFactory(serviceRegistry);
        } catch (Exception ex) {
            System.err.println("Hibernate Exception: " + ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }

}
