package com.energy.smartmeter.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("adminInfo")
@Data
public class Admin {
    @Field("username")
    private String username;

    @Field("password")
    private String password;
}
