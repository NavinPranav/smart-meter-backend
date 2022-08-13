package com.energy.smartmeter.service.impl;

import com.energy.smartmeter.dao.ConsumerDao;
import com.energy.smartmeter.dao.SmartMeterDao;
import com.energy.smartmeter.dto.ConsumerDto;
import com.energy.smartmeter.entity.Consumer;
import com.energy.smartmeter.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Autowired
    private ConsumerDao consumerDao;

    @Autowired
    private SmartMeterDao smartMeterDao;


    @Override
    public void addConsumer(ConsumerDto consumerDto) throws Exception {

        try {
            if(consumerDao.findConsumer(consumerDto.getUsername()) == null) {
                Consumer consumer = new Consumer();
                List<String> smartMeter = Collections.<String>emptyList();
                consumer.setUsername(consumerDto.getUsername());
                consumer.setFirstName(consumerDto.getFirstName());
                consumer.setLastName(consumerDto.getLastName());
                consumer.setMeterId(smartMeter);
                consumerDao.addConsumer(consumer);
            } else {
                throw new Exception("Consumer already exists");
            }
        }catch(Exception e) {
            throw new Exception("Internal Server error");
        }
    }

    @Override
    public Consumer consumerLogin(ConsumerDto consumerDto) throws Exception {
        try {
            if(Objects.nonNull(consumerDao.findConsumer(consumerDto.getUsername()))) {
                return consumerDao.findConsumer(consumerDto.getUsername());
            } else {
                throw new Exception("username is incorrect");
            }
        } catch(Exception e) {
            throw new Exception("Internal server error");
        }
    }

    @Override
    public List<Consumer> getAll() {
        return consumerDao.getAll();
    }

    @Override
    public Consumer getConsumer(String id) throws Exception {
        try {
                return consumerDao.getConsumer(id);
        } catch (Exception e) {
            throw new Exception("Consumer not found");
        }
    }


}
