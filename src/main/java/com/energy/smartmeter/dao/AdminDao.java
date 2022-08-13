package com.energy.smartmeter.dao;

import com.energy.smartmeter.entity.Admin;
import com.energy.smartmeter.dto.AdminDto;
import com.energy.smartmeter.entity.Provider;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AdminDao {
    Admin findAdmin(AdminDto adminDto);

    Admin findAdminByUsername(String username);

    void addAdmin(Admin admin);

    Provider findProvider(String providerName);

    void addProvider(Provider provider);

    List<Provider> getAllProviders();

    void changeProviderStatus(String providerName, Boolean status);

    List<Admin> getAllAdmins();
}
