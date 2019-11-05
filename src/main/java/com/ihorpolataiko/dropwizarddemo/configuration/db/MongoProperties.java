package com.ihorpolataiko.dropwizarddemo.configuration.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MongoProperties {
    private String dbName;
    private String host;
    private Integer port;
}
