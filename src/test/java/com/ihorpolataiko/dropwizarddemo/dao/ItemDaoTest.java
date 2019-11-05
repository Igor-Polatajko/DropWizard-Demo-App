package com.ihorpolataiko.dropwizarddemo.dao;

import com.ihorpolataiko.dropwizarddemo.domain.Item;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;


public class ItemDaoTest extends BaseDaoTest {
    private Map<String, Item> testData;
    private ItemDao itemDao;

    @Before
    public void setup() {
        super.setup();
        List<Item> testDataList = prepareTestData();
        testData = persistTestData(testDataList);
        itemDao = new ItemDao(mongoProperties);
    }

    @After
    public void after() {
        super.after();
    }

    @Test
    public void findAll() {
        List<Item> dataFromDb = itemDao.findAll();
        assertThat(dataFromDb).isEqualTo(new ArrayList<>(testData.values()));
    }

    @Test
    public void findById() {
        String id = testData.keySet().iterator().next();
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
        String id = testData.keySet().iterator().next();
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
        String id = testData.keySet().iterator().next();
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
        String id = testData.keySet().iterator().next();
        Item itemToDelete = itemDao.findById(new ObjectId(id));
        assertNotNull(itemToDelete);

        itemDao.deleteById(new ObjectId(id));

        Item deletedItem = itemDao.findById(new ObjectId(id));
        assertNull(deletedItem);
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
}