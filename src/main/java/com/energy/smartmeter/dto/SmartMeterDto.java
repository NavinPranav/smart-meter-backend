package com.energy.smartmeter.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class SmartMeterDto {
    @Id
    private String id;
    private String meterId;
    private String username;
    private String provider;
    private String status;
    private List<ReadingDto> readings;
}
