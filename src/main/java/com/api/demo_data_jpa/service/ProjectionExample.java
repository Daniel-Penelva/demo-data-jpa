package com.api.demo_data_jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;

import com.api.demo_data_jpa.dto.AuthorDTO;
import com.api.demo_data_jpa.model.Author;
import com.api.demo_data_jpa.projection.AuthorView;
import com.api.demo_data_jpa.repository.AuthorRepository;

import jakarta.transaction.Transactional;

//@Component
public class ProjectionExample implements CommandLineRunner {

        @Autowired
        private AuthorRepository authorRepository;

        @Override
        @Transactional
        public void run(String... args) throws Exception {

                var author1 = Author.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .email("john@gmail.com")
                                .age(35)
                                .build();

                var author2 = Author.builder()
                                .firstName("Daniel")
                                .lastName("Penelva")
                                .email("daniel@gmail.com")
                                .age(31)
                                .build();

                var author3 = Author.builder()
                                .firstName("Fabiana")
                                .lastName("Silva")
                                .email("fabiana@gmail.com")
                                .age(29)
                                .build();

                var author4 = Author.builder()
                                .firstName("Daniel")
                                .lastName("Mota")
                                .email("daniel.mota@gmail.com")
                                .age(25)
                                .build();

                authorRepository.saveAll(List.of(author1, author2, author3, author4));

                /* ===== Projeção Baseada em Interface ===== */
                System.out.println("\n ==== Projeção Baseada em Interface ==== ");

                // 1) Exemplo 1: Buscar autores com idade menor ou igual a 30 anos
                List<AuthorView> authors = authorRepository.findByAgeLessThanEqual(32);

                // Exemplo de uso de projeção de atributos para obter apenas o primeiro nome,
                // email e idade dos autores
                System.out.println("\n ==== Buscar autores com projeção de atributos ==== ");

                System.out.println(
                                "Total de autores encontrados com idade menor ou igual a 30 anos: " + authors.size());
                for (AuthorView a : authors) {
                        System.out.println("Nome: " + a.getFirstName()
                                        + " | Sobrenome: " + a.getEmail()
                                        + " | Idade: " + a.getAge());
                }

                // 2) Buscar autore(s) pelo nome exato
                System.out.println("\n ==== Buscar autores pelo nome ==== ");

                List<AuthorView> authorsByName = authorRepository.findByFirstName("Daniel");

                System.out.println("Total de autores encontrados com nome 'Daniel': " + authorsByName.size());

                for (AuthorView a : authorsByName) {
                        System.out.println("Nome: " + a.getFirstName()
                                        + " | Email: " + a.getEmail());
                }

                /*
                 * // Ou pode fazer assim também:
                 * 
                 * authors.forEach(a -> System.out.println(
                 *      a.getFirstName() 
                 *      + " | " + a.getAge() 
                 *      + " | " + a.getEmail()));
                 */


                /* ===== Projeção Dinâmica ===== */
                System.out.println("\n ==== Projeção Dinâmica ==== ");

                // 1) Projeção Dinâmica com o tipo de retorno AuthorView
                List<AuthorView> views = authorRepository.findByAgeLessThan(30, AuthorView.class);

                System.out.println("\n ==== Projeção Dinâmica com o tipo de retorno AuthorView ====");

                if (views.isEmpty()) {
                        System.out.println("Nenhum autor encontrado com idade menor que 30 anos.");
                        return;
                } else {
                        System.out.println("Total de autores encontrados com idade menor que 30 anos: " + views.size());
                        views.forEach(a -> System.out.println(
                                        "Nome: " + a.getFirstName()
                                        + " | Idade: " + a.getAge()));
                }


                // 2) Projeção Dinâmica com o tipo de retorno AuthorDTO
                List<AuthorDTO> dtos = authorRepository.findByAgeLessThan(35, AuthorDTO.class);

                System.out.println("\n ==== Projeção Dinâmica com o tipo de retorno AuthorDTO ====");

                if (dtos.isEmpty()) {
                        System.out.println("Nenhum autor encontrado com idade menor que 35 anos.");
                } else {
                        System.out.println("Total de autores encontrados com idade menor que 35 anos: " + dtos.size());

                        dtos.forEach(dto -> System.out.println(
                                        "Nome: " + dto.firstName()
                                        + " | Idade: " + dto.age()));
                }
        }
}
