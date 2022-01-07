package com.blackmc.blackbedwars.database;

import com.blackmc.blackbedwars.core.FriendList;
import com.blackmc.blackbedwars.core.Party;
import com.blackmc.blackbedwars.core.PartyMember;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DatabaseManager {

    private static Database database;

    public static void registerDatabaseConnection() {
        
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
            .configure()
            .build();

        MetadataSources sources = new MetadataSources(standardRegistry);
        sources.addAnnotatedClass(FriendList.class);
        sources.addAnnotatedClass(PartyMember.class);
        sources.addAnnotatedClass(Party.class);
        Metadata metadata = sources.buildMetadata();

        SessionFactory sessionFactory = metadata.buildSessionFactory();
        DatabaseManager.database = new Database(sessionFactory);
    }

    public static Database getDatabase() {
        return database;
    }

}