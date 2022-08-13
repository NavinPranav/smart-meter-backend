package com.energy.smartmeter.service;

import com.energy.smartmeter.dto.ConsumerDto;
import com.energy.smartmeter.dto.SmartMeterDto;
import com.energy.smartmeter.dto.Status;
import com.energy.smartmeter.entity.Consumer;

import java.util.List;

public interface ConsumerService {

    void addConsumer(ConsumerDto consumerDto) throws Exception;

    Consumer consumerLogin(ConsumerDto consumerDto) throws Exception;

    List<Consumer> getAll();

    Consumer getConsumer(String id) throws Exception;


}
