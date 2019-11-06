package com.ihorpolataiko.dropwizarddemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDateTime;


@Data
@Entity
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    private ObjectId id;
    private String header;
    private String data;
    private LocalDateTime updatedDate;
    private LocalDateTime createdDate;
}
