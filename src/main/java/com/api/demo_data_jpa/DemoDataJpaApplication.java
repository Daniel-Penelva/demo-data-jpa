package com.api.demo_data_jpa;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.api.demo_data_jpa.model.Author;
import com.api.demo_data_jpa.repository.AuthorRepository;

@SpringBootApplication
@EnableJpaAuditing
public class DemoDataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoDataJpaApplication.class, args);
	}

	//@Bean
	public CommandLineRunner commandLineRunner(AuthorRepository authorRepository) {
		return args -> {
			var author1 = Author.builder()
					.firstName("Daniel")
					.lastName("Penelva")
					.email("daniel.penelva@gmail.com")
					.age(37)
					.build();

			var author2 = Author.builder()
					.firstName("Paulo")
					.lastName("Silva")
					.email("paulo.silva@gmail.com")
					.age(33)
					.build();
			
			var author3 = Author.builder()
					.firstName("Vanessa")
					.lastName("da Cunha")
					.email("vanessa.cunha@gmail.com")
					.age(28)
					.build();
			
			// Exemplo de inserção de autores - Salva os autores no banco de dados
			authorRepository.save(author1);
			authorRepository.save(author2);
			authorRepository.save(author3);

			// Exemplo de busca de todos os autores
			List<Author> authors = Arrays.asList(author1, author2, author3);
			
			for(Author author : authors) {
				System.out.println("Author ID: " + author.getId());
				System.out.println("Author: " + author.getFirstName() + " " + author.getLastName());
				System.out.println("Email: " + author.getEmail());
				System.out.println("Age: " + author.getAge());
				System.out.println("-----------------------------");
			}

			// Ou opção 2: usando lambda
			/*
			authors.forEach(author -> {
				System.out.println("Author ID: " + author.getId());
				System.out.println("Author: " + author.getFirstName() + " " + author.getLastName());
				System.out.println("Email: " + author.getEmail());
				System.out.println("Age: " + author.getAge());
				System.out.println("-----------------------------");
			});
			*/

			// Exemplo de busca por ID
			Author authorById = authorRepository.findById(1).orElse(null);
			if (authorById != null) {
				System.out.println("Autor encontrado por ID: " + authorById.getFirstName() + " " + authorById.getLastName());
			} else {
				System.out.println("Autor não encontrado por ID.");
			}

			// Exemplo de exclusão por ID
			authorRepository.deleteById(2);
			System.out.println("Autor com ID 2 excluído.");
			
			// Exemplo de busca por email
			Author authorByEmail = authorRepository.findByEmail("daniel.penelva@gmail.com");
			if (authorByEmail != null) {
				System.out.println("Autor encontrado por e-mail: " + authorByEmail.getFirstName() + " " + authorByEmail.getLastName());
			} else {
				System.out.println("Autor não encontrado por e-mail.");
			}

			// Exemplo alterar autor por id
			Author authorToUpdate = authorRepository.findById(1).orElse(null);
			if (authorToUpdate != null) {
				authorToUpdate.setFirstName("Daniel Updated");
				authorToUpdate.setLastName("Penelva Updated");
				authorToUpdate.setEmail("daniel.penelva@gmail.com");
				authorToUpdate.setAge(38);
				authorRepository.save(authorToUpdate);
				System.out.println("Autor atualizado: " + authorToUpdate.getFirstName() + " " + authorToUpdate.getLastName());
			} else {
				System.out.println("Autor não encontrado para atualização.");
			}
	
		};
	}

}

/*Anotação:
 * @SpringBootApplication: Anotação que marca a classe como uma aplicação Spring Boot.
 *      - scanBasePackages: Especifica os pacotes a serem escaneados pelo Spring para componentes, configurações e serviços.
 * 	    - exclude: Permite excluir classes de configuração específicas do Spring Boot.
 * @Bean: Indica que o método deve ser registrado como um bean no contexto do Spring.
 * CommandLineRunner: Interface que permite executar código após a inicialização do aplicativo Spring Boot.
 *      - args: Argumentos de linha de comando passados para o aplicativo.
 * AuthorRepository: Interface que estende JpaRepository para operações CRUD na entidade Author.
 *      - save: Método para salvar uma entidade Author no banco de dados.
 *      - findAll: Método para recuperar todas as entidades Author do banco de dados.
 *      - findById: Método para recuperar uma entidade Author pelo ID.
 *      - deleteById: Método para excluir uma entidade Author pelo ID.
 *      - findByEmail: Método personalizado para encontrar um Author pelo email.
*/