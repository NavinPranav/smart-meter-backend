package com.energy.smartmeter.service.impl;

import com.energy.smartmeter.dao.AdminDao;
import com.energy.smartmeter.dao.SmartMeterDao;
import com.energy.smartmeter.dto.AdminDto;
import com.energy.smartmeter.dto.ProviderDto;
import com.energy.smartmeter.entity.Admin;
import com.energy.smartmeter.entity.Provider;
import com.energy.smartmeter.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SmartMeterDao smartMeterDao;


    @Override
    public String adminLogin(AdminDto adminDto) throws Exception {

        try {
            if (adminDao.findAdmin(adminDto) != null) {
                Admin admin = adminDao.findAdmin(adminDto);
                System.out.println(passwordEncoder.encode(adminDto.getPassword()) +  "---"  +admin.getPassword());
                if(passwordEncoder.matches(adminDto.getPassword(), admin.getPassword())) {
                    return "Logged in success";
                } else {
                    throw new Exception("Password is incorrect");
                }
            } else {
                throw new Exception("Username is incorrect");
            }
        } catch ( Exception e) {
            throw new Exception();
        }


    }

    @Override
    public void addAdmin(AdminDto adminDto) throws Exception {
        if(adminDao.findAdmin(adminDto) == null) {
            Admin admin = new Admin();
            admin.setUsername(adminDto.getUsername());
            admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
            try {
                adminDao.addAdmin(admin);
            } catch (Exception e) {
                throw new Exception("Internal server error");
            }

        } else {
            throw new Exception("Username already exists");
        }
    }

    @Override
    public void addProvider(ProviderDto providerDto) throws Exception {
        try{
            if(adminDao.findProvider(providerDto.getName()) == null) {
                Provider provider = new Provider();
                provider.setName(providerDto.getName());
                provider.setRate(providerDto.getRate());
                provider.setActive(true);
                adminDao.addProvider(provider);
            } else {
                throw new Exception("Provider already exists");
            }
        } catch (Exception e) {
            throw new Exception("Internal server error");
        }
    }

    @Override
    public List<Provider> getAllProviders() {
        return adminDao.getAllProviders();
    }

    @Override
    public void changeProviderStatus(String providerName, Boolean status) throws Exception{
        try {
            adminDao.changeProviderStatus(providerName,status);
        } catch (Exception e) {
            throw new Exception("Internal server error");
        }
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminDao.getAllAdmins();
    }


}
