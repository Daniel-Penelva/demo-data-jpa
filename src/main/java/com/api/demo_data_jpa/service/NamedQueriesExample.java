package com.api.demo_data_jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.api.demo_data_jpa.model.Author;
import com.api.demo_data_jpa.repository.AuthorRepository;

import jakarta.transaction.Transactional;

//@Component
public class NamedQueriesExample implements CommandLineRunner {

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

        var author5 = Author.builder()
                .firstName("Daniel")
                .lastName("Mota")
                .email("danielmota@gmail.com")
                .age(34)
                .build();

        authorRepository.saveAll(List.of(author1, author2, author3, author4, author5));

        // 1) Buscar Autor por E-mail
        System.out.println("\n=== Exemplo 1: Buscar Autor por E-mail ===");

        authorRepository.buscarPorEmail("pedro@gmail.com").ifPresentOrElse(
                a -> System.out.println("E-mail Encontrado: " + a.getEmail()),
                () -> System.out.println("E-mail não encontrado"));

        // Ou pode fazer assim - código menos limpo:
        System.out.println("\n=== Exemplo 2: Buscar Autor por E-mail ===");
        var authorByEmail = authorRepository.buscarPorEmail("danielmota@gmail.com");
        if (authorByEmail.isPresent()) {
            System.out.println("E-mail Encontrado: " + authorByEmail.get().getEmail());
        } else {
            System.out.println("E-mail Não Encontrado.");
        }


        // 2) Buscar Autor por Nome

        System.out.println("\n=== Exemplo 1: Buscar Autor por nome ===");
        authorRepository.buscarPorNome("Daniel").forEach(a -> System.out.println(
                "Nome: " + a.getFirstName()
                        + " - Idade: " + a.getAge()));

        // Ou pode fazer assim condicionando
        System.out.println("\n=== Exemplo 2: Buscar Autor por nome ===");
        List<Author> autores = authorRepository.buscarPorNome("Daniel");
        if (!autores.isEmpty()) {
            autores.forEach(a -> System.out.println("Nome encontrado: " + a.getFirstName()));
        } else {
            System.out.println("Nome não encontrado.");
        }


        // 3) Buscar por idade maior que...
        System.out.println("\n=== Exemplo 1: Buscar por idade maior que... ===");
        authorRepository.buscarPorIdadeMaiorQue(30).forEach(System.out::println);

        // Ou pode fazer assim:
        System.out.println("\n=== Exemplo 2: Buscar por idade maior que... ===");
        authorRepository.buscarPorIdadeMaiorQue(35).forEach(a -> System.out.println(
                "Nome: " + a.getFirstName()
                        + " - Idade: " + a.getAge()));


        // 4) Contar Por nome
        System.out.println("\n=== Contar Por nome ===");
        long contarNome = authorRepository.contarPorNome("Daniel");
        System.out.println("São ao todo: " + contarNome + " nome(s) encontrado(s)");

    }

}
