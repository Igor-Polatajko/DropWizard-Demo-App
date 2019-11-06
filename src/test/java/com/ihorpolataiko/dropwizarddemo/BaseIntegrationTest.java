package com.ihorpolataiko.dropwizarddemo;

import com.ihorpolataiko.dropwizarddemo.configuration.db.MongoProperties;
import com.ihorpolataiko.dropwizarddemo.domain.Item;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class BaseIntegrationTest {
    private static final String DATABASE_NAME = "items_test_db";
    private MongoClient mongoClient;
    protected Datastore datastore;
    protected MongoProperties mongoProperties;
    protected Map<String, Item> testData;

    protected void baseSetup() {
        mongoProperties = prepareMongoProperties();
        mongoClient = new MongoClient(mongoProperties.getHost(), mongoProperties.getPort());
        datastore = new Morphia().createDatastore(mongoClient, DATABASE_NAME);
        List<Item> testDataList = prepareTestData();
        testData = persistTestData(testDataList);
    }

    protected void baseAfter() {
        mongoClient.dropDatabase(DATABASE_NAME);
    }

    protected List<Item> getTestDataValues() {
        return new ArrayList<>(testData.values());
    }


    protected <K, V> K getFirstKey(Map<K, V> data) {
        return data.keySet().iterator().next();
    }

    private List<Item> prepareTestData() {
        return IntStream.range(0, 10)
                .mapToObj(i -> Item.builder()
                        .header("header" + i)
                        .data("data" + i)
                        .build())
                .collect(Collectors.toList());
    }

    private Map<String, Item> persistTestData(List<Item> testData) {
        return testData.stream()
                .collect(Collectors.toMap(
                        item -> datastore.save(item).getId().toString(),
                        Function.identity(),
                        (x, y) -> y, LinkedHashMap::new));
    }

    private MongoProperties prepareMongoProperties() {
        return MongoProperties.builder()
                .dbName(DATABASE_NAME)
                .host("localhost")
                .port(27017)
                .build();
    }
}
