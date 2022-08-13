package com.energy.smartmeter.dao.impl;

import com.energy.smartmeter.dao.ConsumerDao;
import com.energy.smartmeter.entity.Consumer;
import com.energy.smartmeter.entity.SmartMeter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConsumerDaoImpl implements ConsumerDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Consumer findConsumer(String consumerName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(consumerName));
        return mongoTemplate.findOne(query, Consumer.class);
    }

    @Override
    public void addConsumer(Consumer consumer) {
        mongoTemplate.save(consumer);
    }

    @Override
    public void updateSmartMeterForConsumer(String consumerId, List<String> updateMeter) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(consumerId));
        Update update = new Update();
        update.set("meterId", updateMeter);
        mongoTemplate.updateFirst(query, update, Consumer.class);

    }

    @Override
    public List<Consumer> getAll() {
        return mongoTemplate.findAll(Consumer.class);
    }

    @Override
    public Consumer getConsumer(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, Consumer.class);
    }


}
