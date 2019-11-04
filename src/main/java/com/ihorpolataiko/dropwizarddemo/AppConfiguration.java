package com.ihorpolataiko.dropwizarddemo;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AppConfiguration extends Configuration {
    @JsonProperty("swagger")
    private SwaggerBundleConfiguration swaggerBundleConfiguration;
}
