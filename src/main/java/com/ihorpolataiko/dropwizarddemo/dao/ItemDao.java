package com.ihorpolataiko.dropwizarddemo.dao;

import com.ihorpolataiko.dropwizarddemo.configuration.db.MongoProperties;
import com.ihorpolataiko.dropwizarddemo.domain.Item;
import com.mongodb.MongoClient;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.UpdateOperations;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ItemDao {

    private final Datastore datastore;

    @Inject
    public ItemDao(@Named("mongo-properties") MongoProperties mongoProperties) {
        MongoClient mongoClient = new MongoClient(mongoProperties.getHost(), mongoProperties.getPort());
        Morphia morphia = new Morphia();
        this.datastore = morphia.createDatastore(mongoClient, mongoProperties.getDbName());
    }

    public List<Item> findAll() {
        return datastore.find(Item.class).asList();
    }

    public Item findById(ObjectId id) {
        return datastore.createQuery(Item.class)
                .filter("id", id)
                .get();
    }

    public void update(Item item) {
        UpdateOperations<Item> updateOperations = datastore.createUpdateOperations(Item.class)
                .set("header", item.getHeader())
                .set("data", item.getData())
                .set("updatedDate", LocalDateTime.now());
        datastore.update(findById(item.getId()), updateOperations);
    }

    public void updateField(ObjectId objectId, Map<String, Object> fieldsToUpdate) {
        UpdateOperations<Item> updateOperations = datastore.createUpdateOperations(Item.class);
        fieldsToUpdate.forEach(updateOperations::set);
        updateOperations.set("updatedDate", LocalDateTime.now());
        datastore.update(findById(objectId), updateOperations);
    }

    public String create(Item item) {
        LocalDateTime currentTime = LocalDateTime.now();
        item.setUpdatedDate(currentTime);
        item.setCreatedDate(currentTime);
        return datastore.save(item).getId().toString();
    }

    public void deleteById(ObjectId objectId) {
        Item itemToDelete = findById(objectId);
        datastore.delete(itemToDelete);
    }
}
