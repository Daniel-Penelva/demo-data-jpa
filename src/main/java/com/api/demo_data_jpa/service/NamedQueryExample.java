package com.api.demo_data_jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;

import com.api.demo_data_jpa.model.Course;
import com.api.demo_data_jpa.repository.CourseRepository;

import jakarta.transaction.Transactional;

//@Component
public class NamedQueryExample implements CommandLineRunner {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Course course1 = Course.builder()
                .name("Programação Orientado a Objeto")
                .description("Texto sobre POO")
                .build();

        Course course2 = Course.builder()
                .name("Programação JAVA")
                .description("Texto sobre Programação JAVA")
                .build();

        Course course3 = Course.builder()
                .name("Arquitetura de Software")
                .description("Texto sobre Arquitetura de Software")
                .build();

        Course course4 = Course.builder()
                .name("Modelagem de Dados")
                .description("Texto sobre Modelagem de dados")
                .build();

        courseRepository.saveAll(List.of(course1, course2, course3, course4));

        // Buscar Cursos por Nome
        System.out.println("\n === Buscando Curso por nome ===");
        courseRepository.buscarPorNome("Arquitetura de Software").ifPresentOrElse(
                c -> System.out.println("Curso Encontrado: " + c.getName()),
                () -> System.out.println("Curso não encontrado"));
    }

}
