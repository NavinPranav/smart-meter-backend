package com.energy.smartmeter.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document("consumer")
@Data
public class Consumer {

    @Id
    private String id;
    @Field("fistName")
    private String firstName;
    @Field("lastName")
    private String lastName;
    @Field("username")
    private String username;
    @Field("meterId")
    private List<String> meterId;
}
