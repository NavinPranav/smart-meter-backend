package com.energy.smartmeter.controller;

import com.energy.smartmeter.controller.response.ApiResponse;
import com.energy.smartmeter.dto.AdminDto;
import com.energy.smartmeter.dto.ProviderDto;
import com.energy.smartmeter.entity.Provider;
import com.energy.smartmeter.service.AdminService;
import com.energy.smartmeter.util.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private AdminService energyManagementService;

    @Autowired
    private JwtUtility jwtUtility;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> adminLogin(@RequestBody AdminDto adminDto) throws Exception {
        ApiResponse response = new ApiResponse();
        try {
            String logMessage = energyManagementService.adminLogin(adminDto);
            response.setMessage(logMessage);
            HttpHeaders httpHeaders = new HttpHeaders();
            response.setData(jwtUtility.generateToken(adminDto, 10*60*60));
            httpHeaders.set("jwttoken",jwtUtility.generateToken(adminDto, 10*60*60));
            return new ResponseEntity<ApiResponse>(response,httpHeaders,HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage(String.valueOf(e));
            return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add-admin")
    public ResponseEntity<ApiResponse> addAdmin(@RequestHeader(value = "authorization") String auth, @RequestBody AdminDto adminDto) {
        ApiResponse response = new ApiResponse();
        if(jwtUtility.validateAdminToken(auth)) {
            try {
                energyManagementService.addAdmin(adminDto);
                response.setMessage("Admin created successfully");
                return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
            } catch (Exception e) {
                response.setMessage(String.valueOf(e));
                return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/add-provider")
    public ResponseEntity<ApiResponse> addProvider(@RequestHeader(value = "authorization") String auth, @RequestBody ProviderDto providerDto) {
        ApiResponse response = new ApiResponse();
        if(jwtUtility.validateAdminToken(auth)) {
            try {
                energyManagementService.addProvider(providerDto);
                response.setMessage("Provider created successfully");
                return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
            } catch (Exception e) {
                response.setMessage(String.valueOf(e));
                return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("get-all/providers")
    public ResponseEntity<ApiResponse> getAllProviders() {
        ApiResponse response = new ApiResponse();
        response.setData(energyManagementService.getAllProviders());
        return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);

    }

    @PutMapping("/provider/{providerName}")
    public ResponseEntity<ApiResponse> changeProviderStatus(@PathVariable String providerName, @RequestBody Provider provider) {
        ApiResponse response = new ApiResponse();
        try {
            energyManagementService.changeProviderStatus(providerName, provider.getActive());
            response.setMessage("Operation done");
            return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage(String.valueOf(e));
            return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-all/admins")
    public ResponseEntity<ApiResponse> getAllAdmins(@RequestHeader(value = "authorization") String auth) {
        ApiResponse response = new ApiResponse();
        if(jwtUtility.validateAdminToken(auth)) {
            response.setData(energyManagementService.getAllAdmins());
            return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);
        }
    }

 }
