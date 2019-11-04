package com.ihorpolataiko.dropwizarddemo.domain;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Item {
    private ObjectId id;
    private String header;
    private String data;
    private String createdDate;
}
