package com.example.demo.dataprocessor.controller;

import com.example.demo.dataprocessor.jpa.entity.DataProcessor;
import com.example.demo.dataprocessor.service.DataProcessorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DataProcessorControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createDataProcessor_shouldReturnOk_andCallService_whenValidDataProvided() throws Exception {
        // Arrange
        DataProcessorService mockService = Mockito.mock(DataProcessorService.class);
        DataProcessorController controller = new DataProcessorController(mockService);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        DataProcessor dto = new DataProcessor();
        dto.setUserId(1);
        dto.setDpValue(12345L);


        // Act & Assert
        mockMvc.perform(post("/api/v1/example")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response")
                        .value("DataProcessor created successfully"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        // Verify service is called once
        Mockito.verify(mockService, times(1))
                .createDataProcessor(any(DataProcessor.class));
    }

    @Test
    void createDataProcessor_shouldReturnBadRequest_whenDpValueIsNull() throws Exception {
        // Arrange
        DataProcessorService mockService = Mockito.mock(DataProcessorService.class);
        DataProcessorController controller = new DataProcessorController(mockService);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        DataProcessor dto = new DataProcessor();
        dto.setUserId(1);
        dto.setDpValue(null);  // Invalid: dpValue is null

        // Act & Assert
        mockMvc.perform(post("/api/v1/example")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response")
                        .value("DataProcessor doesn't exist"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.status").value("FAILURE"));

        // Verify service is NOT called when validation fails
        Mockito.verify(mockService, times(0))
                .createDataProcessor(any(DataProcessor.class));
    }
}
