package com.energy.smartmeter.dao;

import com.energy.smartmeter.entity.Consumer;

import java.util.List;

public interface ConsumerDao {
    Consumer findConsumer(String consumerName);


    void addConsumer(Consumer consumer);
    void updateSmartMeterForConsumer(String consumerId, List<String> updateMeter);

    List<Consumer> getAll();

    Consumer getConsumer(String id);
}
