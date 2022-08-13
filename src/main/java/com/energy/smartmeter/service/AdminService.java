package com.energy.smartmeter.service;

import com.energy.smartmeter.dto.*;
import com.energy.smartmeter.entity.Admin;
import com.energy.smartmeter.entity.Provider;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AdminService {
    String adminLogin(AdminDto adminDto) throws Exception;

    void addAdmin(AdminDto adminDto) throws Exception;

    void addProvider(ProviderDto providerDto) throws Exception;

    List<Provider> getAllProviders();

    void changeProviderStatus(String providerName, Boolean status) throws Exception;

    List<Admin> getAllAdmins();


}
