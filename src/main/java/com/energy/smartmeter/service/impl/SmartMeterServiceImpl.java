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
import org.springframework.web.client.HttpClientErrorException;

import java.text.SimpleDateFormat;
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
                       throw new Exception("provider does not exist");
                    }


                } else {
                    throw new Exception("Username does not exists");
                }
            } else {
                throw new Exception("Smart meter already present");
            }

        } catch (Exception e) {
            throw new Exception("Internal service error");
        }


    }

    @Override
    public void smartMeterReadings(String meterId, ReadingDto readingDto) throws Exception {
       try {
           if(Objects.nonNull(smartMeterDao.findSmartMeter(meterId))) {
               smartMeterDao.updateSmartMeterReading(meterId, readingDto);
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
                    throw new Exception("Smart meter rejected");

                } else if(smartMeter.getStatus().equals("NEWLY_ADDED") && !updateStatus.getNewMeter()) {
                    throw new Exception("Smart meter not installed for enable/disable");
                }
                else if (!smartMeter.getStatus().equals("NEWLY_ADDED") && updateStatus.getNewMeter()){
                    throw new Exception("Smart meter already exist");
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
                    throw new Exception("Provider is disabled contact admin");
                }
            } else {
                throw new Exception("Smart meter or provider does not exist");
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
            HashMap<String, String> meter = new HashMap<>();
            SmartMeter smartMeter = smartMeterDao.findSmartMeter(meterId);
            meter.put("meterId", smartMeter.getMeterId());
            meter.put("username", smartMeter.getUsername());
            meter.put("provider", smartMeter.getProvider());
            meter.put("status", smartMeter.getStatus());
            smartMeters.add(meter);
        }
        return smartMeters;
    }

    @Override
    public Double calculate(String meterId) throws Exception {
        try {
            SmartMeter smartMeter = smartMeterDao.findSmartMeter(meterId);
            List<ReadingDto> readings = smartMeter.getReadings();
            Integer previousTime = 0;
            Double totalReading = 0.0;
            for(int i = 1; i<readings.size(); i++) {
                if(readings.get(i).getTime() != null) {
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    Date date1 = format.parse(readings.get(previousTime).getTime());
                    Date date2 = format.parse(readings.get(i).getTime());
                    long difference = date2.getTime() - date1.getTime();
                    double seconds = difference/1000.0;
                    double hours = seconds/(60.0 * 60.0);
                    Double kw = readings.get(i).getKw() + 0.0;
                    Provider provider = adminDao.findProvider(readings.get(i).getProvider());
                    totalReading += (kw * hours) * provider.getRate();
                    previousTime+=1;
                } else {
                    throw new Exception("Value not found");
                }
            }
            return totalReading;
        } catch (HttpClientErrorException.NotFound e) {
            throw new Exception("smart meter not found");
        }
    }
}  
