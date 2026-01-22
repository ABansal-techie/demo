package com.example.demo.dataprocessor.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "data_processor")
public class DataProcessor {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "dpValue")
    private Long dpValue;

    public DataProcessor() {}

    public DataProcessor(Integer userId, Long dpValue) {
        this.userId = userId;
        this.dpValue = dpValue;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getDpValue() {
        return dpValue;
    }

    public void setDpValue(Long dpValue) {
        this.dpValue = dpValue;
    }

}
