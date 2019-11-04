package com.ihorpolataiko.dropwizarddemo.transfer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ihorpolataiko.dropwizarddemo.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private String id;
    private String header;
    private String data;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedDate;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdDate;

    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId().toString())
                .header(item.getHeader())
                .data(item.getData())
                .updatedDate(item.getUpdatedDate())
                .createdDate(item.getCreatedDate())
                .build();
    }

    public static Item fromDto(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId() != null ? new ObjectId(itemDto.getId()) : null)
                .header(itemDto.getHeader())
                .data(itemDto.getData())
                .updatedDate(itemDto.getUpdatedDate())
                .createdDate(itemDto.getCreatedDate())
                .build();
    }
}
