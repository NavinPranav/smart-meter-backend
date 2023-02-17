package com.energy.smartmeter.controller;

import com.energy.smartmeter.controller.response.ApiResponse;
import com.energy.smartmeter.dto.ConsumerDto;
import com.energy.smartmeter.entity.Consumer;
import com.energy.smartmeter.service.ConsumerService;
import com.energy.smartmeter.util.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private JwtUtility jwtUtility;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createConsumer(@RequestHeader(value = "authorization") String auth, @RequestBody ConsumerDto consumerDto) {
        ApiResponse response = new ApiResponse();
        if (jwtUtility.validateAdminToken(auth)) {
            try {
                consumerService.addConsumer(consumerDto);
                response.setMessage("Consumer created successfully");
                return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
            } catch (Exception e) {
                response.setMessage(String.valueOf(e));
                return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> consumerLogin(@RequestBody ConsumerDto consumerDto) {
        ApiResponse response = new ApiResponse();
        try {
            Consumer consumer = consumerService.consumerLogin(consumerDto);
            response.setMessage("Logged in successfully");
            response.setData(consumer);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("jwttoken", jwtUtility.generateToken(consumerDto, 10 * 60 * 60));
            return new ResponseEntity<ApiResponse>(response, httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage(String.valueOf(e));
            return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse> getALlConsumer() {
        ApiResponse response = new ApiResponse();
        List<Consumer> consumers = consumerService.getAll();
        response.setData(consumers);
        return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getConsumer(@PathVariable String id) {
        ApiResponse response = new ApiResponse();
        try {
            Consumer consumer = consumerService.getConsumer(id);
            response.setData(consumer);
            return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
