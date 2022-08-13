package com.energy.smartmeter.service;

import com.energy.smartmeter.dto.ConsumerDto;
import com.energy.smartmeter.dto.ReadingDto;
import com.energy.smartmeter.dto.SmartMeterDto;
import com.energy.smartmeter.dto.Status;
import com.energy.smartmeter.entity.SmartMeter;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public interface SmartMeterService {
    String addSmartMeter(SmartMeterDto smartMeterDto) throws Exception;

    void smartMeterReadings(String meterId, ReadingDto readingDto) throws Exception;

    String changeSmartMeterStatus(String smartMeterId, Status updateStatus) throws Exception;

    String changeProvider(String smartMeterId, String providerName) throws Exception;

    List<SmartMeter> getAllSmartMeters ();

    SmartMeter getById(String id) throws Exception;

    List<HashMap<String, String>> getSmartMeterById(String id);

    Double calculate(String meterId) throws Exception;

}
