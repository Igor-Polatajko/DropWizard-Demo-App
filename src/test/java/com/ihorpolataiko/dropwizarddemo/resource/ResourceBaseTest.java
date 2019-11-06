package com.ihorpolataiko.dropwizarddemo.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ihorpolataiko.dropwizarddemo.App;
import com.ihorpolataiko.dropwizarddemo.AppConfiguration;
import com.ihorpolataiko.dropwizarddemo.BaseIntegrationTest;
import io.dropwizard.testing.junit.DropwizardAppRule;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public abstract class ResourceBaseTest extends BaseIntegrationTest {
    private OkHttpClient okHttpClient;
    private HttpUrl httpUrl;
    private ObjectMapper mapper;
    private static final URI BASE_URI = URI.create("http://localhost:10000");
    @ClassRule
    public static final DropwizardAppRule<AppConfiguration> RULE =
            new DropwizardAppRule<>(App.class, "src/test/resources/config-test.yml");

    @Before
    public void setup() throws MalformedURLException {
        baseSetup();
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();

        httpUrl = HttpUrl.parse(BASE_URI.toURL().toString());
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @After
    public void after() {
        baseAfter();
    }

    protected HttpUrl getHttpUrl(String... pathVariables) {
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        Stream.of(pathVariables).forEach(urlBuilder::addPathSegment);
        return urlBuilder.build();
    }

    protected Response performCall(Request request) throws IOException {
        return okHttpClient.newCall(request).execute();
    }

    protected RequestBody createRequestBody(Object requestBody) throws JsonProcessingException {
        return RequestBody.create(okhttp3.MediaType.parse(APPLICATION_JSON), mapper.writeValueAsString(requestBody));
    }

    protected <T> T retrieveFromResponseBody(@NotNull ResponseBody responseBody, TypeReference<T> clazz)
            throws IOException {
        return mapper.readValue(responseBody.string(), clazz);
    }
}
