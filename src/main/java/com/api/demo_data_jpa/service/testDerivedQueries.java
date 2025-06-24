package com.api.demo_data_jpa.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;

import com.api.demo_data_jpa.DemoDataJpaApplication;
import com.api.demo_data_jpa.model.Author;
import com.api.demo_data_jpa.repository.AuthorRepository;
import com.api.demo_data_jpa.repository.FileRepository;

import jakarta.transaction.Transactional;

//@Component
public class testDerivedQueries implements CommandLineRunner {

    private final FileRepository fileRepository;

    private final DemoDataJpaApplication demoDataJpaApplication;

    @Autowired
    private AuthorRepository authorRepository;

    testDerivedQueries(DemoDataJpaApplication demoDataJpaApplication, FileRepository fileRepository) {
        this.demoDataJpaApplication = demoDataJpaApplication;
        this.fileRepository = fileRepository;
    }

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

        // Salvando os autores
        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);
        authorRepository.save(author4);

        // Criando uma lista de Autores
        List<Author> authors = new ArrayList<>();

        // Adicionando os autores numa lista de Autores
        authors.add(author1);
        authors.add(author2);
        authors.add(author3);
        authors.add(author4);

        // Gerar uma lista de autores
        System.out.println("=== Criando Autor ===");
        authors.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | Sobrenome: " + a.getLastName()
                + " | Email: " + a.getEmail()
                + " | Idade: " + a.getAge()));

        // 1) Buscar un autor por email
        System.out.println("\n=== Buscar Autor por Email ===");
        var authorByEmail = authorRepository.findByEmail("marcelo@gmail.com");
        System.out.println("Dados do Autor - Nome: " + authorByEmail.getFirstName()
                + " | E-mail: " + authorByEmail.getEmail());

        
        // 2) Buscar por nome exato
        System.out.println("\n=== Buscar por nome exato ===");

        List<Author> authorsByName = authorRepository.findAllByFirstName("Daniel");
        authorsByName.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()));

        
        // 3) Buscar por nome exato ignorando letras maiúsculas e minúsculas
        System.out.println("\n=== Buscar por nome exato ignorando letras maiúsculas");
        List<Author> authorsByNameIgnoringCase = authorRepository.findByFirstNameIgnoreCase("maria");
        authorsByNameIgnoringCase.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()));


        // 4) Buscar por nome contendo o nome ignorando letras maiúsculas e minúsculas
        System.out.println("\n=== Buscar por nome contendo o nome e ignorando letras maiúsculas e minúsculas");
        List<Author> authorsByNameContaining = authorRepository.findByFirstNameContainingIgnoreCase("ma");
        if (authorsByNameContaining != null && !authorsByNameContaining.isEmpty()) {
            authorsByNameContaining.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()));
        } else {
            System.out.println("Nenhum autor encontrado");
        }


        // 5) Buscar por todos os nomes começando com a palavra "m" (LiKE 'm%') ignorando letras maiúsculas e minúsculas.
        System.out.println("\n=== Buscar por todos os nomes começando com a(s) letra(s) e ignorando letras maiúsculas e minúsculas");
        List<Author> authorsByNameStartingWith = authorRepository.findByFirstNameStartsWithIgnoreCase("p");
        if (authorsByNameStartingWith != null && !authorsByNameStartingWith.isEmpty()) {
            authorsByNameStartingWith.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()));
        } else {
            System.out.println("Nenhum autor encontrado");
        }


        // 6) Buscar por todos com nome terminando com a palavra "o" e ignorando letras maiúsculas e minúsculas.
        System.out.println("\n=== Buscar por todos com nome terminando a(s) letra(s) e ignorando letras maiúsculas e minúsculas.");
        List<Author> authorsByNameEndingWith = authorRepository.findByFirstNameEndsWithIgnoreCase("o");
        if (authorsByNameEndingWith != null && !authorsByNameEndingWith.isEmpty()) {
            authorsByNameEndingWith.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()));
        } else {
            System.out.println("Nenhum autor encontrado");
        }


        // 7) Buscar por nome exato utilizando uma lista de valores
        System.out.println("\n=== Buscar por nome exato utilizando uma lista de valores");
        List<String> names = Arrays.asList("Daniel", "Maria");
        List<Author> authorsByNameIn = authorRepository.findByFirstNameInIgnoreCase(names);
        if (authorsByNameIn != null && !authorsByNameIn.isEmpty()) {
            authorsByNameIn.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()));
        } else {
            System.out.println("Nenhum autor encontrado");
        }


        // 8) Buscar por nome exato ou por sobrenome exato ignorando letras maiúsculas e minúsculas.
        System.out.println("\n=== Buscar por nome exato ou por sobrenome exato ignorando letras maiúsculas e minúsculas.");
        List<Author> authorsByNameOrLastName = authorRepository.findByFirstNameOrLastNameIgnoreCase("daniel", "penelva");
        if (authorsByNameOrLastName != null && !authorsByNameOrLastName.isEmpty()) {
            authorsByNameOrLastName.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()));
        } else {
            System.out.println("Nenhum autor encontrado");
        }


        // 9) Buscar por idade maior que...
        System.out.println("\n === Buscar por idade maior que...");
        List<Author> authorsByAgeGreaterThan = authorRepository.findByAgeGreaterThan(30);
        if (authorsByAgeGreaterThan != null && !authorsByAgeGreaterThan.isEmpty()) {
            authorsByAgeGreaterThan.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()
                + " | Idade: " + a.getAge()));
        } else {
            System.out.println("Nenhum autor encontrado");
        }


        // 10) Buscar por idade entre...
        System.out.println("\n === Buscar por idade entre...");
        List<Author> authorsByAgeBetween = authorRepository.findByAgeBetween(20, 35);
        if (authorsByAgeBetween != null && !authorsByAgeBetween.isEmpty()) {
            authorsByAgeBetween.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()
                + " | Idade: " + a.getAge()));
        } else {
            System.out.println("Nenhum autor encontrado");
        }


        // 11) Buscar por email contendo...
        System.out.println("\n === Buscar por email contendo...");
        List<Author> authorsByEmailContaining = authorRepository.findByEmailContaining("gmail.com");
        if (authorsByEmailContaining != null && !authorsByEmailContaining.isEmpty()) {
            authorsByEmailContaining.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()));
        } else {
            System.out.println("Nenhum autor encontrado");
        }


        // 12) Verificar se existe autor com email.
        System.out.println("\n === Verificar se existe autor com email.");
        boolean existsAuthorByEmail = authorRepository.existsByEmail("davi.@gmail.com");
        if (!existsAuthorByEmail){
            System.out.println("NÃO EXISTE autor com o e-mail");
        } else {
            System.out.println("Existe autor com o e-mail");
        }


        // 13) Contar autores com idade maior que...
        System.out.println("\n === Contar autores com idade maior que...");
        Long countAuthors = authorRepository.countByAgeGreaterThan(30);
        if (countAuthors == 0) {
            System.out.println("NÃO FOI ENCONTRADO autores com idade maior que 35 anos!");
        } else {
            System.out.println("FOI ENCONTRADO " + countAuthors + " autor(es) com idade maior que 35 anos!");
        }

        /*Então: 
         *  Daniel (37) -> 37 é maior que 35 = 1
         *  Marcelo (35) -> 35 é maior que 35 = 1
         *  Pedro (27) -> 27 é menor que 30 = 0
         *  Maria (31) -> 30 é maior que 36 = 1 
         *                                     / 3 autores ao todo
        */


        // 14) Contar autores com idade maior ou igual a...
        System.out.println("\n === Contar autores com idade maior ou igual a...");
        Long countAuthors2 = authorRepository.countByAgeGreaterThanEqual(35);
        if (countAuthors2 == 0) {
            System.out.println("NÃO FOI ENCONTRADO autores com idade maior que 35 anos!");
        } else {
            System.out.println("FOI ENCONTRADO " + countAuthors2 + " autor(es) com idade maior ou igual que 35 anos!");
        }

        /*Então: 
         *  Daniel (37) -> 37 é maior que 35 = 1
         *  Marcelo (35) -> 35 é igual a 35 = 1
         *  Pedro (27) -> 27 é menor que 35 = 0
         *  Maria (31) -> 30 é menor que 35 = 0 
         *                                     / 2 autores ao todo
        */


        // 15) Buscar todos com o nome começando com...
        System.out.println("\n === Buscar todos com o nome começando com...");
        List<Author> authorsByFirstNameStartingWith = authorRepository.findAllByFirstNameStartingWith("M");
        if (authorsByFirstNameStartingWith.isEmpty()) {
            System.out.println("NÃO FOI ENCONTRADO autor com o nome começando com M");
        } else {
            authorsByFirstNameStartingWith.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()));
            System.out.println("FOI ENCONTRADO " + authorsByFirstNameStartingWith.size() + " autor(es) com o nome começando com M");
        }
        
        
    }

}
