package com.energy.smartmeter.service.impl;

import com.energy.smartmeter.dao.AdminDao;
import com.energy.smartmeter.dao.ConsumerDao;
import com.energy.smartmeter.dao.SmartMeterDao;
import com.energy.smartmeter.dto.ReadingDto;
import com.energy.smartmeter.dto.SmartMeterDto;
import com.energy.smartmeter.dto.Status;
import com.energy.smartmeter.entity.Consumer;
import com.energy.smartmeter.entity.Provider;
import com.energy.smartmeter.entity.SmartMeter;
import com.energy.smartmeter.service.SmartMeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SmartMeterServiceImpl implements SmartMeterService {

    @Autowired
    private SmartMeterDao smartMeterDao;

    @Autowired
    private ConsumerDao consumerDao;

    @Autowired
    private AdminDao adminDao;

    @Override
    public String addSmartMeter(SmartMeterDto smartMeterDto) throws Exception {

        try {
            if(smartMeterDao.findSmartMeter(smartMeterDto.getMeterId()) == null) {
                if(Objects.nonNull(consumerDao.findConsumer(smartMeterDto.getUsername()))) {
                    if(Objects.nonNull(adminDao.findProvider(smartMeterDto.getProvider()))) {
                        List<ReadingDto> readings = Collections.<ReadingDto>emptyList();
                        SmartMeter smartMeter = new SmartMeter();

                        smartMeter.setUsername(smartMeterDto.getUsername());
                        smartMeter.setMeterId(smartMeterDto.getMeterId());
                        smartMeter.setProvider(smartMeterDto.getProvider());
                        smartMeter.setStatus("NEWLY_ADDED");
                        smartMeter.setReadings(readings);
                        smartMeterDao.addSmartMeter(smartMeter);
                        return "Smart meter added successfully";
                    } else {
                       return "provider does not exist";
                    }


                } else {
                    return "Username does not exists";
                }
            } else {
                return "Smart meter already present";
            }

        } catch (Exception e) {
            throw new Exception("Internal service error");
        }


    }

    @Override
    public void smartMeterReadings(String meterId, ReadingDto readingDto) throws Exception {
        System.out.println(readingDto);
       try {
           if(Objects.nonNull(smartMeterDao.findSmartMeter(meterId))) {
               smartMeterDao.updateSmartMeterReading(meterId, readingDto);


//               SmartMeter smartMeter = smartMeterDao.findSmartMeter(meterId);
//               String provider = smartMeter.getProvider();
//               List<Integer> readings = smartMeter.getReadings();
//               Provider providerData = smartMeterDao.findProvider(provider);
//               Integer totalReadings = readings.size();
//               if(totalReadings > 0) {
//
//               } else {
//                   readings.add(providerData.getRate());
//               }
//               System.out.println(readings);
//               return providerData;
           }
       } catch (Exception e) {
           throw new Exception("Internal server error");
       }
    }

    @Override
    public String changeSmartMeterStatus(String smartMeterId, Status updateStatus) throws Exception {
        try {
            SmartMeter smartMeter = smartMeterDao.findSmartMeter(smartMeterId);

                if(smartMeter.getStatus().equals("NEWLY_ADDED") && updateStatus.getNewMeter()) {
                    smartMeterDao.updateStatus(smartMeter.getMeterId(), updateStatus.getStatus());
                    Consumer consumer = consumerDao.findConsumer(smartMeter.getUsername());
                    if(updateStatus.getStatus().equals("APPROVED")) {
                        consumer.getMeterId().add(smartMeterId);
                        consumerDao.updateSmartMeterForConsumer(consumer.getId(), consumer.getMeterId());
                        smartMeterDao.updateStatus(smartMeter.getMeterId(), "APPROVED");
                        return "Smart meter approved";
                    }
                    smartMeterDao.updateStatus(smartMeter.getMeterId(), "REJECTED");
                    return "Smart meter rejected";

                } else if(smartMeter.getStatus().equals("NEWLY_ADDED") && !updateStatus.getNewMeter()) {
                    return "Smart meter not installed for enable/disable";
                }
                else if (!smartMeter.getStatus().equals("NEWLY_ADDED") && updateStatus.getNewMeter()){
                    return "Smart meter already exist";
                } else {
                    smartMeterDao.updateStatus(smartMeter.getMeterId(), updateStatus.getStatus());
                    return updateStatus + "successfully";
                }
        } catch (Exception e) {
            throw new Exception("Smart meter not found");
        }
    }

    @Override
    public String changeProvider(String smartMeterId, String providerName) throws Exception {
        try {
            Provider provider = adminDao.findProvider(providerName);
            if((Objects.nonNull(smartMeterDao.findSmartMeter(smartMeterId) != null)) && (provider != null)) {
                if(provider.getActive()) {
                    smartMeterDao.changeProvider(smartMeterId, providerName);
                    return "Provider changed successfully";
                } else {
                    return "Provider is disabled contact admin";
                }
            } else {
                return "Smart meter or provider does not exist";
            }
        } catch (Exception e) {
            throw new Exception("Internal server error");
        }

    }

    @Override
    public List<SmartMeter> getAllSmartMeters() {
        return smartMeterDao.getAllSmartMeters();
    }

    @Override
    public SmartMeter getById(String id) throws Exception{
        try {
            return smartMeterDao.getById(id);
        } catch (Exception e){
            throw new Exception("Smart meter not found");
        }
    }

    @Override
    public List<HashMap<String, String>> getSmartMeterById(String id) {
        List<HashMap<String, String>> smartMeters = new ArrayList<>();
        Consumer consumer = consumerDao.getConsumer(id);
        for(String meterId: consumer.getMeterId()) {
            System.out.println(meterId);
            HashMap<String, String> meter = new HashMap<>();
            SmartMeter smartMeter = smartMeterDao.findSmartMeter(meterId);
            System.out.println(smartMeter);
            meter.put("meterId", smartMeter.getMeterId());
            meter.put("username", smartMeter.getUsername());
            meter.put("provider", smartMeter.getProvider());
            meter.put("status", smartMeter.getStatus());
            smartMeters.add(meter);
        }
        System.out.println(smartMeters);
        return smartMeters;
    }
}  
