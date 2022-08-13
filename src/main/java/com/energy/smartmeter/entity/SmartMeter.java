package com.energy.smartmeter.entity;

import com.energy.smartmeter.dto.ReadingDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document("smart-meter")
@Data
public class SmartMeter {

    @Id
    private String id;
    @Field("meterId")
    private String meterId;
    @Field("username")
    private String username;
    @Field("provider")
    private String provider;
    @Field("status")
    private String status;
    @Field("readings")
    private List<ReadingDto> readings;
}
