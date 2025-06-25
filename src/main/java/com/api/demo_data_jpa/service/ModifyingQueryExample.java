package com.api.demo_data_jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.api.demo_data_jpa.model.Author;
import com.api.demo_data_jpa.repository.AuthorRepository;

import jakarta.transaction.Transactional;

@Component
public class ModifyingQueryExample implements CommandLineRunner{

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        
        var author1 = Author.builder()
                .firstName("Daniel")
                .lastName("Penelva")
                .email("daniel@gmail.com")
                .age(37)
                .build();

        var author2 = Author.builder()
                .firstName("Marcelo")
                .lastName("Silva")
                .email("marcelo@gmail.com")
                .age(35)
                .build();

        var author3 = Author.builder()
                .firstName("Pedro")
                .lastName("Mota")
                .email("pedro@gmail.com")
                .age(27)
                .build();

        var author4 = Author.builder()
                .firstName("Maria")
                .lastName("Nunes")
                .email("maria@gmail.com")
                .age(31)
                .build();

        authorRepository.saveAll(List.of(author1, author2, author3, author4));

        System.out.println("\n=== Lista de Autores ===");
        authorRepository.findAll().forEach(a -> System.out.println(
            "ID: " + a.getId() 
            + " | Nome: " + a.getFirstName() 
            + " | Idade: " + a.getAge()
        ));

        // 1) Atualizar nome do author
        System.out.println("\n=== Atualizar nome do autor ===");
        authorRepository.updateFirstNameById(1, "Daniel Updated");

        authorRepository.findAll().forEach(a -> System.out.println(
            "ID: " + a.getId() 
            + " | Nome: " + a.getFirstName()
        ));

        // 2) Atualizar idade do author
        System.out.println("\n=== Atualizar idade do autor ===");
        authorRepository.updateAgeById(1, 31);

        authorRepository.findAll().forEach(a -> System.out.println(
            "ID: " + a.getId() 
            + " | Nome: " + a.getFirstName() 
            + " | Idade: " + a.getAge()
        ));


        // 3) Deletar autores com idade menor que 30
        System.out.println("\n=== Deletar autores com idade menor que 30 ===");
        authorRepository.deleteAuthorsYoungerThan(30);
        

        System.out.println("\n=== Depois do delete ===");
        
        authorRepository.findAll().forEach(a -> System.out.println(
            "ID: " + a.getId() 
            + " | Nome: " + a.getFirstName() 
            + " | Idade: " + a.getAge()
        ));

    }   
}
