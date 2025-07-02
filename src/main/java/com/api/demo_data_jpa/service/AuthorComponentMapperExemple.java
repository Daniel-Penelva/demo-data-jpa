package com.api.demo_data_jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;

import com.api.demo_data_jpa.dto.AuthorDTO;
import com.api.demo_data_jpa.mapper.AuthorComponentMapper;
import com.api.demo_data_jpa.model.Author;
import com.api.demo_data_jpa.repository.AuthorRepository;

import jakarta.transaction.Transactional;

//@Component
public class AuthorComponentMapperExemple implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorComponentMapper authorMapper;

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

        List<Author> authors = authorRepository.findAll();

        // Convertendo para AuthorDTO usando o componente Mapper
        // (aqui é usado uma abordagem manual, mas poderia ser feito com MapStruct ou ModelMapper)
        //Exemplo 1 - Não é uma boa prática, mas é para fins de demonstração
        /* 
        List<AuthorDTO> authorDTOs = authors.stream()
                .map(author -> new AuthorDTO(
                        author.getFirstName(),
                        author.getLastName(),
                        author.getEmail(),
                        author.getAge()))
                .toList();
        */

        // Exemplo 2 - Usando o AuthorMapper - uma boa prática separa a lógica de conversão em um componente Mapper
        List<AuthorDTO> authorDTOs = authorMapper.toDTOList(authors);

        authorDTOs.forEach(dto -> System.out.println(
            "Nome: " + dto.firstName()
            + " | Sobrenome: " + dto.lastName()
            + " | Email: " + dto.emailAddress()
            + " | Idade: " + dto.age()
        ));
    }
}


