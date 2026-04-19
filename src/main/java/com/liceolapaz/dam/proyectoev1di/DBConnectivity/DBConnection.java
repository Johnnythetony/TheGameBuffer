package com.liceolapaz.dam.proyectoev1di.DBConnectivity;

import com.liceolapaz.dam.proyectoev1di.Entities.*;
import io.github.cdimascio.dotenv.Dotenv;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.HashMap;
import java.util.Map;

public class DBConnection
{
    private static SessionFactory session_factory;
    private Session session;

    protected DBConnection()
    {
        if (session_factory == null)
        {
            createSessionFactory();
        }
    }

    protected void createSessionFactory()
    {
        Dotenv dotenv = Dotenv.configure()
                .directory(this.getClass().getClassLoader().getResource(".env").toString())
                .filename(".env")
                .load();

        Map<String, Object> settings = new HashMap<>();

        settings.put("hibernate.connection.url", URLAssembly.assemble());
        settings.put("hibernate.connection.driver_class", dotenv.get("DB_DRIVER_MYSQL"));
        settings.put("hibernate.connection.username", dotenv.get("DB_USER_MYSQL"));
        settings.put("hibernate.connection.password", dotenv.get("DB_PASSWORD_MYSQL"));
        settings.put("hibernate.dialect",dotenv.get("DB_DIALECT"));
        settings.put("hibernate.hbn2ddl.auto", dotenv.get("HBN2DDL_AUTO"));
        settings.put("hibernate.show_sql", "true");
        settings.put("hibernate.format_sql", "true");

        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().applySettings(settings).build();

        session_factory = new MetadataSources(ssr)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Videogame.class)
                .addAnnotatedClass(Company.class)
                .addAnnotatedClass(Backlog.class)
                .addAnnotatedClass(Platform.class)
                .addAnnotatedClass(GamesPlatforms.class)
                .addAnnotatedClass(VideojuegoPlataformaId.class)
                .buildMetadata().buildSessionFactory();
    }

    protected Session getSession()
    {
        if(session == null)
        {
            session = session_factory.openSession();
        }
        return session;
    }

    protected void initTransaction()
    {
        if(session_factory == null)
        {
            createSessionFactory();
        }
        getSession().getTransaction().begin();
    }

    protected void commitTransaction()
    {
        getSession().getTransaction().commit();
        session.close();
        session = null;
    }

    protected void rollbackTransaction()
    {
        getSession().getTransaction().rollback();
        session.close();
        session = null;
    }
}
