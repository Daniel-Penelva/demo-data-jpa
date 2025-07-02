package com.api.demo_data_jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.api.demo_data_jpa.dto.AuthorDTO;
import com.api.demo_data_jpa.mapper.AuthorMapper;
import com.api.demo_data_jpa.model.Author;
import com.api.demo_data_jpa.repository.AuthorRepository;

import jakarta.transaction.Transactional;

@Component
public class AuthorMapperExample implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        List<Author> authors = List.of(
                // Criando autores
                Author.builder()
                        .firstName("Daniel")
                        .lastName("Penelva")
                        .email("daniel@gmail.com")
                        .age(37)
                        .build(),

                Author.builder()
                        .firstName("Maria")
                        .lastName("Nunes")
                        .email("maria@gmail.com")
                        .age(25)
                        .build(),

                Author.builder()
                        .firstName("Carlos")
                        .lastName("Silva")
                        .email("carlos@gmail.com")
                        .age(28)
                        .build());

        authorRepository.saveAll(authors);

        // 1) Exemplo 1 - Usando Mapstruct para mapear de Author para AuthorDTO
        List<AuthorDTO> authorDTOs = AuthorMapper.INSTANCE.toDtoList(authors);

        authorDTOs.forEach(dto -> System.out.println(
                "Nome: " + dto.firstName() +
                " | Sobrenome: " + dto.lastName() +
                " | Email: " + dto.emailAddress() +
                " | Idade: " + dto.age()
        ));

    }

}
