package com.example.demo.dataprocessor.jpa.repo;

import com.example.demo.dataprocessor.jpa.entity.DataProcessor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataProcessorRepo extends CrudRepository <DataProcessor,Integer>{

}
