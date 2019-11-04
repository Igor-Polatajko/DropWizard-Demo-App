package com.ihorpolataiko.dropwizarddemo.dao;

import com.ihorpolataiko.dropwizarddemo.domain.Item;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ItemDao {

    private List<Item> items = new ArrayList<>();

    public List<Item> findAll() {
        return items;
    }

    public Item findById(ObjectId objectId) {
        return items.stream()
                .filter(i -> i.getId() == objectId)
                .findAny()
                .orElse(null);
    }

    public Item update(Item item) {
        deleteById(item.getId());
        items.add(item);
        return item;
    }

    public Item create(Item item) {
        item.setId(new ObjectId());
        items.add(item);
        return item;
    }

    public void deleteById(ObjectId objectId) {
        Item itemFromDb = findById(objectId);
        items.remove(itemFromDb);
    }
}
