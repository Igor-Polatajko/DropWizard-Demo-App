package com.ihorpolataiko.dropwizarddemo.resource;

import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GreetingResourceE2ETest extends ResourceBaseTest {

    @Test
    public void defaultHello() throws IOException {
        Request request = new Request.Builder()
                .url(getHttpUrl("greeting"))
                .get()
                .build();

        Response response = performCall(request);

        assertEquals("Hello world!", response.body().string());
    }

    @Test
    public void namedHello() throws IOException {
        String nameForTest = "Somebody's name";
        Request request = new Request.Builder()
                .url(getHttpUrl("greeting", nameForTest))
                .get()
                .build();

        Response response = performCall(request);

        assertEquals("Hello, " + nameForTest + "!", response.body().string());
    }

}