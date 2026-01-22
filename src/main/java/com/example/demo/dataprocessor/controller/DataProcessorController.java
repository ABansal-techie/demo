package com.example.demo.dataprocessor.controller;

import com.example.demo.dataprocessor.jpa.entity.DataProcessor;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.dataprocessor.service.DataProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class DataProcessorController {

    private static final Logger log = LoggerFactory.getLogger(DataProcessorController.class);

    private final DataProcessorService dataProcessorService;

    public DataProcessorController(DataProcessorService dataProcessorService){
        this.dataProcessorService = dataProcessorService;
    }


    @PostMapping("/example")
    public ApiResponse createDataProcessor(@RequestBody DataProcessor dataProcessor){
        log.info("DataProcessorController createDataProcessor() started");
        String requestId = UUID.randomUUID().toString();
        ApiResponse response = null;
        try {
            // Validate: fail if dataProcessor is null or has empty required fields
            if(dataProcessor == null || dataProcessor.getUserId() == null || dataProcessor.getDpValue() == null){
                return ApiResponse.builder()
                        .status("FAILURE")
                        .response("DataProcessor doesn't exist")
                        .statusCode(400)
                        .requestId(requestId)
                        .build();
            }
            dataProcessorService.createDataProcessor(dataProcessor);
            response = ApiResponse.builder()
                    .status("SUCCESS")
                    .response("DataProcessor created successfully")
                    .statusCode(200)
                    .requestId(requestId)
                    .build();
            log.info("Response:" + response.getResponse());
        } catch (Exception e) {
            log.error("Exception in DataProcessorController createDataProcessor(): ", e);
            response = ApiResponse.builder()
                    .response("Error processing DataProcessor")
                    .statusCode(500)
                    .status("FAILURE")
                    .requestId(requestId)
                    .build();
        }
        return response;

    }

}
