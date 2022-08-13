package com.energy.smartmeter.dao;

import com.energy.smartmeter.dto.ReadingDto;
import com.energy.smartmeter.dto.Status;
import com.energy.smartmeter.entity.Provider;
import com.energy.smartmeter.entity.SmartMeter;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SmartMeterDao {
    void addSmartMeter(SmartMeter smartMeter);

    SmartMeter findSmartMeter(String meterId);

    Provider findProvider(String id);

    void updateStatus (String smartMeterId, String updateStatus);

    void changeProvider(String smartMeterId, String providerName);

    List<SmartMeter> getAllSmartMeters();

    SmartMeter getById(String id);

    void updateSmartMeterReading(String meterId, ReadingDto readingDto);


}

