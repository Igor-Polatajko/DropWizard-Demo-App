package com.ihorpolataiko.dropwizarddemo.dao;

import com.ihorpolataiko.dropwizarddemo.configuration.db.MongoProperties;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class BaseDaoTest {
    private static final String DATABASE_NAME = "items_test_db";
    private MongoClient mongoClient;
    protected Datastore datastore;
    protected MongoProperties mongoProperties;

    public void setup() {
        mongoProperties = prepareMongoProperties();
        mongoClient = new MongoClient(mongoProperties.getHost(), mongoProperties.getPort());
        datastore = new Morphia().createDatastore(mongoClient, DATABASE_NAME);
    }

    public void after() {
        mongoClient.dropDatabase(DATABASE_NAME);
    }

    private MongoProperties prepareMongoProperties() {
        return MongoProperties.builder()
                .dbName(DATABASE_NAME)
                .host("localhost")
                .port(27017)
                .build();
    }
}
