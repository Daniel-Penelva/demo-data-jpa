package com.api.demo_data_jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.demo_data_jpa.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>{

    /* ==== Utilizando @NamedQuery ==== */ 

    @Query(name = "Course.findByName")
    Optional<Course> buscarPorNome(@Param("name") String name);
    
}
