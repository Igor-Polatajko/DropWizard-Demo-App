package com.ihorpolataiko.dropwizarddemo.configuration.db;

import lombok.Data;

@Data
public class MongoProperties {
    private String dbName;
    private String host;
    private Integer port;
}
