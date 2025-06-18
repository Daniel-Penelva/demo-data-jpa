package com.api.demo_data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.demo_data_jpa.model.Resource;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer>{
    
}
