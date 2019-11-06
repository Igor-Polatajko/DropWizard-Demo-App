package com.ihorpolataiko.dropwizarddemo.dao;

import com.ihorpolataiko.dropwizarddemo.BaseIntegrationTest;
import com.ihorpolataiko.dropwizarddemo.domain.Item;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;


public class ItemDaoTest extends BaseIntegrationTest {
    private ItemDao itemDao;

    @Before
    public void setup() {
        super.baseSetup();
        itemDao = new ItemDao(mongoProperties);
    }

    @After
    public void after() {
        super.baseAfter();
    }

    @Test
    public void findAll() {
        List<Item> dataFromDb = itemDao.findAll();
        assertThat(dataFromDb).isEqualTo(getTestDataValues());
    }

    @Test
    public void findById() {
        String id = getFirstKey(testData);
        Item itemFromDb = itemDao.findById(new ObjectId(id));
        assertEquals(testData.get(id), itemFromDb);
    }

    @Test
    public void create() {
        Item itemToCreate = Item.builder()
                .header("header")
                .data("data")
                .build();
        String id = itemDao.create(itemToCreate);
        Item itemFromDb = itemDao.findById(new ObjectId(id));
        assertThat(itemToCreate).isEqualToIgnoringGivenFields(itemFromDb,
                "createdDate", "updatedDate");
    }

    @Test
    public void update() {
        String id = getFirstKey(testData);
        Item itemToUpdate = itemDao.findById(new ObjectId(id));
        assertNotNull(itemToUpdate);
        itemToUpdate.setHeader("New Header");
        itemToUpdate.setData("New Data");

        itemDao.update(itemToUpdate);

        Item updatedItem = itemDao.findById(new ObjectId(id));
        assertNotSame(itemToUpdate, updatedItem);
        assertThat(updatedItem).isEqualToIgnoringGivenFields(itemToUpdate,
                "updatedDate");
    }

    @Test
    public void updateField() {
        String id = getFirstKey(testData);
        Item itemToUpdate = itemDao.findById(new ObjectId(id));
        assertNotNull(itemToUpdate);

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("header", "new header");

        itemDao.updateField(itemToUpdate.getId(), fieldsToUpdate);

        Item updatedItem = itemDao.findById(new ObjectId(id));
        assertNotEquals(itemToUpdate, updatedItem);
        assertNotEquals(itemToUpdate.getUpdatedDate(), updatedItem.getUpdatedDate());
        assertEquals(fieldsToUpdate.get("header"), updatedItem.getHeader());
        assertEquals(itemToUpdate.getData(), updatedItem.getData());
    }

    @Test
    public void deleteById() {
        String id = getFirstKey(testData);
        Item itemToDelete = itemDao.findById(new ObjectId(id));
        assertNotNull(itemToDelete);

        itemDao.deleteById(new ObjectId(id));

        Item deletedItem = itemDao.findById(new ObjectId(id));
        assertNull(deletedItem);
    }
}