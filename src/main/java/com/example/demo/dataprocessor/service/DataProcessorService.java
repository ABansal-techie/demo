package com.example.demo.dataprocessor.service;

import com.example.demo.dataprocessor.jpa.entity.DataProcessor;
import com.example.demo.common.response.ApiResponse;


public interface DataProcessorService {

    public ApiResponse createDataProcessor(DataProcessor dataProcessor);
}
