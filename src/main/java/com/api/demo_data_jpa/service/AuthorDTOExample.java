package com.api.demo_data_jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.api.demo_data_jpa.dto.AuthorDTO;
import com.api.demo_data_jpa.model.Author;
import com.api.demo_data_jpa.repository.AuthorRepository;

import jakarta.transaction.Transactional;

@Component
public class AuthorDTOExample implements CommandLineRunner{

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        
        // Criando autores
        var author1 = Author.builder()
                .firstName("Daniel")
                .lastName("Penelva")
                .email("daniel@gmail.com")
                .age(37)
                .build();

        var author2 = Author.builder()
                .firstName("Maria")
                .lastName("Nunes")
                .email("maria@gmail.com")
                .age(25)
                .build();

        var author3 = Author.builder()
                .firstName("Carlos")
                .lastName("Silva")
                .email("carlos@gmail.com")
                .age(28)
                .build();

        authorRepository.saveAll(List.of(author1, author2, author3));

        System.out.println("\n ==== Buscar autores com DTO ==== ");
        List<AuthorDTO> authorDTOs = authorRepository.buscarAutoresDTO(27);

        authorDTOs.forEach(dto -> System.out.println(
            "Nome: " + dto.firstName() 
            + " | Sobrenome: " + dto.lastName()
            + " | Email: " + dto.email()
            + " | Idade: " + dto.age()
        ));
    }
    
}
