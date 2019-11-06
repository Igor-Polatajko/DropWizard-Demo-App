package com.ihorpolataiko.dropwizarddemo.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ihorpolataiko.dropwizarddemo.domain.Item;
import okhttp3.Request;
import okhttp3.Response;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ItemResourceE2ETest extends ResourceBaseTest {

    @Test
    public void findAll() throws IOException {
        Request request = new Request.Builder()
                .url(getHttpUrl("items"))
                .get()
                .build();

        Response response = performCall(request);
        List<Item> resultList = retrieveFromResponseBody(response.body(), new TypeReference<List<Item>>() {
        });

        assertEquals(200, response.code());
        assertThat(resultList).isEqualTo(getTestDataValues());
    }

    @Test
    public void findById() throws IOException {
        String testId = getFirstKey(testData);

        Request request = new Request.Builder()
                .url(getHttpUrl("items", testId))
                .get()
                .build();

        Response response = performCall(request);
        Item resultItem = retrieveFromResponseBody(response.body(), new TypeReference<Item>() {
        });

        assertEquals(200, response.code());
        assertEquals(testData.get(testId), resultItem);
    }

    @Test
    public void update() throws IOException {
        String testId = getFirstKey(testData);
        Item itemToUpdate = testData.get(testId);

        Item updatedItem = Item.builder()
                .header("Updated header")
                .data("Updated data")
                .build();

        Request request = new Request.Builder()
                .url(getHttpUrl("items", testId))
                .put(createRequestBody(updatedItem))
                .build();
        Response response = performCall(request);

        assertEquals(200, response.code());

        Item updatedItemFromDb = queryById(testId);

        assertNotEquals(itemToUpdate.getHeader(), updatedItemFromDb.getHeader());
        assertNotEquals(itemToUpdate.getData(), updatedItemFromDb.getData());
        assertEquals(updatedItem.getHeader(), updatedItemFromDb.getHeader());
        assertEquals(updatedItem.getData(), updatedItemFromDb.getData());
    }

    @Test
    public void updateFields() throws IOException {
        String testId = getFirstKey(testData);
        Item itemToUpdate = testData.get(testId);

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        String newHeader = "Updated header";
        fieldsToUpdate.put("header", newHeader);

        Request request = new Request.Builder()
                .url(getHttpUrl("items", testId))
                .patch(createRequestBody(fieldsToUpdate))
                .build();
        Response response = performCall(request);

        assertEquals(200, response.code());

        Item updatedItemFromDb = queryById(testId);

        assertNotEquals(itemToUpdate.getHeader(), updatedItemFromDb.getHeader());
        assertEquals(newHeader, updatedItemFromDb.getHeader());
        assertEquals(itemToUpdate.getData(), updatedItemFromDb.getData());
    }

    @Test
    public void create() throws IOException {
        String newHeader = "New header";
        String newData = "New data";
        Item itemToCreate = Item.builder()
                .header("New header")
                .data("New data")
                .build();
        List<Item> itemsBefore = queryAll();

        Request request = new Request.Builder()
                .url(getHttpUrl("items"))
                .post(createRequestBody(itemToCreate))
                .build();
        Response response = performCall(request);

        assertEquals(201, response.code());
        List<Item> itemsAfter = queryAll();
        assertThat(itemsBefore.size() == itemsAfter.size() + 1);
        assertThat(itemsBefore)
                .filteredOn(item -> newHeader.equals(item.getHeader()))
                .filteredOn(item -> newData.equals(item.getData()))
                .hasSize(0);
        assertThat(itemsAfter)
                .filteredOn(item -> newHeader.equals(item.getHeader()))
                .filteredOn(item -> newData.equals(item.getData()))
                .hasSize(1);

    }

    @Test
    public void deleteById() throws IOException {
        String testId = getFirstKey(testData);
        Item itemFromDbBeforeDelete = queryById(testId);
        assertNotNull(itemFromDbBeforeDelete);

        Request request = new Request.Builder()
                .url(getHttpUrl("items", testId))
                .delete()
                .build();

        Response response = performCall(request);

        assertEquals(204, response.code());

        Item itemFromDbAfterDelete = queryById(testId);
        assertNull(itemFromDbAfterDelete);
    }

    private Item queryById(String id) {
        return datastore.createQuery(Item.class)
                .filter("id", new ObjectId(id))
                .get();
    }

    private List<Item> queryAll() {
        return datastore.find(Item.class).asList();
    }
}