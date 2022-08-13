package com.energy.smartmeter.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("providers")
@Data
public class Provider {

    @Id
    private String id;

    @Field("name")
    private String name;
    @Field("rate")
    private Integer rate;
    @Field("active")
    private Boolean active;

}
