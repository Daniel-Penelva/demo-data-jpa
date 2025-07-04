package com.api.demo_data_jpa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.api.demo_data_jpa.filter.AuthorBookFilter;
import com.api.demo_data_jpa.filter.AuthorFilter;
import com.api.demo_data_jpa.model.Author;
import com.api.demo_data_jpa.model.Book;
import com.api.demo_data_jpa.repository.AuthorRepository;
import com.api.demo_data_jpa.specification.AuthorSpecifications;

import jakarta.transaction.Transactional;

@Component
public class AuthorSpecificationExample implements CommandLineRunner{

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        authorRepository.saveAll(List.of(
            new Author("Daniel", "Penelva", "daniel.penelva@gmail.com", 37),
            new Author("João", "Silva", "joao.silva@gmail.com", 25),
            new Author("Maria", "Pereira", "maria.pereira@gmail.com", 33),
            new Author("Maria", "Nunes", "maria.nunes@gmail.com", 25),
            new Author("Carlos", "Silva", "carlos@empresa.com", 32),
            new Author("João", "Lima", "joao.lima@gmail.com", 41),
            new Author("Maria", "Moraes", "maria.moraes@gmail.com", 34),
            new Author("Daniel", "Oliveira", "daniel.oliveira@gmail.com", 35),
            new Author("Bolsonaro", "Mito", "bolsonaro@gmail.com", 60),
            new Author("Lula", "Ladrão", "lula.ladrao@gmail.com", 70),
            new Author("Danilo", "Marques", "danilo@gmail.com", 50)
        ));
        
        // 1) Usando Specification para buscar autores com nome 'Daniel' e idade maior que 30 e que o email contenha 'gmail'
        List<Author> specificAuthors = authorRepository.findAll(
            Specification.where(AuthorSpecifications.hasFirstName("Daniel"))
            .and(null != AuthorSpecifications.hasAgeGreaterThan(30) ? AuthorSpecifications.hasAgeGreaterThan(30) : null)
            .and(AuthorSpecifications.emailsContains("gmail"))
        );

        System.out.println("\n === Autores especificados com nome 'Daniel' e idade maior (>) que 30 e email contendo 'gmail'");
        specificAuthors.forEach(a -> System.out.println("Nome: " + a.getFirstName() +
            "| Sobrenome: " + a.getLastName() +
            "| Idade: " + a.getAge() + 
            "| Email: " + a.getEmail()));


        // 2) Usando Paginação e Ordenação

        // A consulta irá retornar uma página de autores com nome 'Daniel', idade maior que 30 e email contendo 'gmail', ordenada por idade em ordem decrescente, com 2 elementos por página.
        Specification<Author> specification = Specification.where(AuthorSpecifications.hasFirstName("Maria"))
            .and(null != AuthorSpecifications.hasAgeGreaterThan(30) ? AuthorSpecifications.hasAgeGreaterThan(30) : null)
            .and(AuthorSpecifications.emailsContains("gmail"));

        Pageable pageable = PageRequest.of(0, 2, Sort.by("age").descending()); // Paginação com 2 elementos por página e ordenação decrescente pela idade

        Page<Author> page = authorRepository.findAll(specification, pageable);  // Usando a lista de autores especificados como filtro.

        
        System.out.println("\n === Autores filtrados com Paginação e Ordenação");
        page.forEach(p -> System.out.println("Nome: " + p.getFirstName() +
            "| Sobrenome: " + p.getLastName() +
            "| Idade: " + p.getAge() + 
            "| Email: " + p.getEmail()));

        System.out.println("Total de Autores: " + page.getTotalElements());
        System.out.println("Total de Páginas: " + page.getTotalPages());
        System.out.println("Página Atual: " + page.getNumber());
        System.out.println("Tamanho da Página: " + page.getSize());
        System.out.println("Tem Próxima Página? " + page.hasNext());
        System.out.println("Tem Página Anterior? " + page.hasPrevious());


        // 3) Filtro com AuthorFilter queé um DTO que contém os critérios de filtro para a pesquisa.

        System.out.println("\n === Filtro com AuthorFilter ===");

        // Criando um AuthorFilter com os critérios de filtro
        AuthorFilter filter = new AuthorFilter();
        filter.setFirstName("Da");
        filter.setEmailFragment("gmail");
        filter.setMinAge(30);
        filter.setMaxAge(61);

        Page<Author> pagina = authorRepository.findAll(AuthorSpecifications.build(filter), pageable);

        System.out.println("\n === Autores filtrados com Paginação e Ordenação");
        pagina.forEach(p -> System.out.println("Nome: " + p.getFirstName() +
            "| Sobrenome: " + p.getLastName() +
            "| Idade: " + p.getAge() + 
            "| Email: " + p.getEmail()));

        System.out.println("Total de Autores: " + pagina.getTotalElements());


        // 4) Filtro com Join para buscar relação entre Autor e Livro
        System.out.println("\n === Filtro com Join entre Autor e Livro ===");

        // Criando Autores e Livros
        var author1 = new Author("Caio", "Roberto", "caio@gmail.com", 36, new ArrayList<>());
        var author2 = new Author("Ana", "Clara", "ana@gmail", 40, new ArrayList<>());

        var book1 = new Book("Java Básico", author1);
        var book2 = new Book("Spring Boot Avançado", author1);
        var book3 = new Book("Clean Code", author2);

        author1.setBooks(List.of(book1, book2));
        author2.setBooks(List.of(book3));

        authorRepository.saveAll(List.of(author1, author2));

        // Filtro Dinâmico com Join
        AuthorBookFilter filterAuthorBook = new AuthorBookFilter();
        filterAuthorBook.setFirstName("Caio");
        filterAuthorBook.setBookTitleFragment("spring");
        filterAuthorBook.setMinAge(30);

        Pageable pageableJoin = PageRequest.of(0, 5);
        var paginaJoin = authorRepository.findAll(AuthorSpecifications.buildBookFilter(filterAuthorBook), pageableJoin);

        System.out.println("\n=== Resultado da busca com JOIN ===");
        paginaJoin.getContent().forEach(a -> System.out.println("Autor: " + a.getFirstName() + 
        " | Idade: " + a.getAge() + 
        " | Nome do Livro:" + a.getBooks().stream()
            .map(Book::getTitle)
            .findFirst()
            .orElse("Nenhum livro encontrado")));
    }
    
}
