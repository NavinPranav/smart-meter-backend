package com.energy.smartmeter.controller;

import com.energy.smartmeter.controller.response.ApiResponse;
import com.energy.smartmeter.dto.ReadingDto;
import com.energy.smartmeter.dto.SmartMeterDto;
import com.energy.smartmeter.dto.Status;
import com.energy.smartmeter.entity.Provider;
import com.energy.smartmeter.service.SmartMeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("smart-meter")
public class SmartMeterController {

    @Autowired
    private SmartMeterService smartMeterService;

    @PostMapping("/install")
    public ResponseEntity<ApiResponse> addSmartMeter(@RequestBody SmartMeterDto smartMeterDto) {
        ApiResponse response = new ApiResponse();
        try {
            String logMessage = smartMeterService.addSmartMeter(smartMeterDto);
            response.setMessage(logMessage);
            return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.setMessage(e.getMessage());
            return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> findByMeterId(@PathVariable String id) {
        ApiResponse response = new ApiResponse();
        try {
            response.setData(smartMeterService.getById(id));
            return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.setMessage(String.valueOf(e));
            return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("/reading/{meterId}")
    public ResponseEntity<ApiResponse> readings(@PathVariable String meterId, @RequestBody ReadingDto readingDto) {
        ApiResponse response = new ApiResponse();
        try {
            smartMeterService.smartMeterReadings(meterId, readingDto);
            response.setMessage("Reading done");
            return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);

        }
    }

    @PutMapping("/update_status/{smartMeterId}")
    public ResponseEntity<ApiResponse> changeSmartMeterStatus(@PathVariable String smartMeterId, @RequestBody Status updateStatus) {
        ApiResponse response = new ApiResponse();
        try {
            String status = smartMeterService.changeSmartMeterStatus(smartMeterId, updateStatus);
            response.setMessage(status);
            return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage(String.valueOf(e));
            return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/change_provider/{smartMeterId}")
    public ResponseEntity<ApiResponse> changeProvider(@PathVariable String smartMeterId, @RequestBody Provider provider) {
        ApiResponse response = new ApiResponse();
        try {
            String status = smartMeterService.changeProvider(smartMeterId, provider.getName());
            response.setMessage(status);
            return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage(String.valueOf(e));
            return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get-all")
    public ResponseEntity<ApiResponse> getAllSmartMeters() {
        ApiResponse response = new ApiResponse();
        response.setData(smartMeterService.getAllSmartMeters());
        return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
    }

    @GetMapping("get-smart-meters/{id}")
    public ResponseEntity<ApiResponse> getSmartMetersById(@PathVariable String id) {
        ApiResponse response = new ApiResponse();
        List<HashMap<String, String>> smartMeters =  smartMeterService.getSmartMeterById(id);
        response.setData(smartMeters);
        return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
    }

    @GetMapping("calculate/{meterId}")
    public ResponseEntity<ApiResponse> calculate(@PathVariable String meterId) throws Exception {
        ApiResponse response = new ApiResponse();
        response.setData(smartMeterService.calculate(meterId));
        return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
    }

}
