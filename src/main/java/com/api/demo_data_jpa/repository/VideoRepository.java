package com.api.demo_data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.demo_data_jpa.model.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer>{
    
}
