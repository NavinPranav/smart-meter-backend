package com.energy.smartmeter.dao.impl;

import com.energy.smartmeter.dao.AdminDao;
import com.energy.smartmeter.dto.AdminDto;
import com.energy.smartmeter.dto.ProviderDto;
import com.energy.smartmeter.entity.Admin;
import com.energy.smartmeter.entity.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminDaoImpl implements AdminDao {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Admin findAdmin(AdminDto adminDto) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(adminDto.getUsername()));
        return mongoTemplate.findOne(query, Admin.class);
    }

    @Override
    public Admin findAdminByUsername(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        return mongoTemplate.findOne(query, Admin.class);
    }

    @Override
    public void addAdmin(Admin admin) {
        mongoTemplate.insert(admin);
    }

    @Override
    public Provider findProvider(String providerName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(providerName));
        return mongoTemplate.findOne(query, Provider.class);
    }

    @Override
    public void addProvider(Provider provider) {
        mongoTemplate.insert(provider);
    }

    @Override
    public List<Provider> getAllProviders() {
        return mongoTemplate.findAll(Provider.class);
    }

    @Override
    public void changeProviderStatus(String providerName, Boolean status) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(providerName));
        Update update = new Update();
        update.set("active", status);
        mongoTemplate.updateFirst(query,update, Provider.class);
    }
    @Override
    public List<Admin> getAllAdmins() {
        return mongoTemplate.findAll(Admin.class);
    }


}
