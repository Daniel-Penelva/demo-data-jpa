package com.api.demo_data_jpa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.api.demo_data_jpa.model.Author;
import com.api.demo_data_jpa.repository.AuthorRepository;
import com.github.javafaker.Faker;

import jakarta.transaction.Transactional;

@Component
public class AuthorFakeDataFaker implements CommandLineRunner{

    @Autowired
    private AuthorRepository authorRepository;

    private final Faker faker = new Faker();


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (authorRepository.count() == 0) {          // Esse if é uma boa prática para não duplicar dados toda vez que reiniciar a aplicação.

            List<Author> authors = new ArrayList<>();

            for(int i=0; i< 50; i++) {
                Author author = Author.builder()
                        .firstName(faker.name().firstName())                 // Um nome aleatório
                        .lastName(faker.name().lastName())                   // Um sobrenome aleatório
                        .email(faker.internet().emailAddress())              // Um e-mail plausível
                        .age(faker.number().numberBetween(20, 60))   // Um número dentro do intervalo de 20 a 60
                        .build();

                authors.add(author);
            }
            
            authorRepository.saveAll(authors);
            System.out.println("=== Fake autores gerados com Faker ===");

            for (Author author : authors) {
                System.out.println("Nome do autor: " + author.getFirstName() 
                    + " | Sobrenome: " + author.getLastName()
                    + " | E-mail: " + author.getEmail()
                    + " | Idade: " + author.getAge());
            }
        }
    }
    
}
