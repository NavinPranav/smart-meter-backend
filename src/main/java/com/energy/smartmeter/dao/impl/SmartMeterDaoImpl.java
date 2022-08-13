package com.energy.smartmeter.dao.impl;

import com.energy.smartmeter.dao.SmartMeterDao;
import com.energy.smartmeter.dto.ReadingDto;
import com.energy.smartmeter.entity.Provider;
import com.energy.smartmeter.entity.SmartMeter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SmartMeterDaoImpl implements SmartMeterDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void addSmartMeter(SmartMeter smartMeter) {
        mongoTemplate.insert(smartMeter);
    }

    @Override
    public SmartMeter findSmartMeter(String meterId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("meterId").is(meterId));
        return mongoTemplate.findOne(query, SmartMeter.class);
    }


    @Override
    public Provider findProvider(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, Provider.class);
    }

    @Override
    public void updateStatus(String smartMeterId, String updateStatus) {
        Query query = new Query();
        query.addCriteria(Criteria.where("meterId").is(smartMeterId));
        Update update = new Update();
        update.set("status", updateStatus);
        mongoTemplate.updateFirst(query, update, SmartMeter.class);

    }

    public void changeProvider(String smartMeterId, String providerName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("meterId").is(smartMeterId));
        Update update = new Update();
        update.set("provider", providerName);
        mongoTemplate.updateFirst(query, update, SmartMeter.class);
    }

    @Override
    public List<SmartMeter> getAllSmartMeters() {
        return mongoTemplate.findAll(SmartMeter.class);
    }

    @Override
    public SmartMeter getById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("meterId").is(id));
        return mongoTemplate.findOne(query, SmartMeter.class);
    }

    @Override
    public void updateSmartMeterReading(String meterId, ReadingDto readingDto) {
        Query query = new Query();
        query.addCriteria(Criteria.where("meterId").is(meterId));
        SmartMeter smartMeter = mongoTemplate.findOne(query, SmartMeter.class);
        String provider = smartMeter.getProvider();
        Update update = new Update();
        readingDto.setProvider(provider);
        update.push("readings", readingDto);
        mongoTemplate.updateFirst(query, update, SmartMeter.class);

    }


}
