package com.energy.smartmeter.dto;

import lombok.Data;

@Data
public class ReadingDto {
    private String date;
    private String time;
    private int kw;
    private String provider;
}
