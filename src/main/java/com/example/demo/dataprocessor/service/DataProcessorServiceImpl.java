package com.example.demo.dataprocessor.service;

import com.example.demo.dataprocessor.jpa.entity.DataProcessor;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.dataprocessor.jpa.repo.DataProcessorRepo;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class DataProcessorServiceImpl implements DataProcessorService {

    private final DataProcessorRepo dataProcessorRepo;

    public DataProcessorServiceImpl(DataProcessorRepo dataProcessorRepo){
            this.dataProcessorRepo = dataProcessorRepo;
    }

    @Override
    public ApiResponse createDataProcessor(DataProcessor dataProcessor) {
        String requestId = UUID.randomUUID().toString();
        if(dataProcessor!=null){
            dataProcessorRepo.save(dataProcessor);
        }else{
            return ApiResponse.builder().response("DataProcessorModel doesn't exist").statusCode(400).requestId(requestId).build();
        }
        return ApiResponse.builder().response("DataProcessorModel created successfully").statusCode(200).requestId(requestId).build();
    }

}
