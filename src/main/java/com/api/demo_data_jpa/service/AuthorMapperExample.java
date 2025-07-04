package com.api.demo_data_jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;

import com.api.demo_data_jpa.dto.AuthorDTO;
import com.api.demo_data_jpa.mapper.AuthorMapper;
import com.api.demo_data_jpa.model.Author;
import com.api.demo_data_jpa.model.embedded.Address;
import com.api.demo_data_jpa.repository.AuthorRepository;

import jakarta.transaction.Transactional;

//@Component
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
                        .address(new Address("Rua das Flores", "123", "12345-678"))
                        .build(),

                Author.builder()
                        .firstName("Maria")
                        .lastName("Nunes")
                        .email("maria@gmail.com")
                        .age(25)
                        .address(new Address("Avenida Brasil", "456", "98765-432"))
                        .build(),

                Author.builder()
                        .firstName("Carlos")
                        .lastName("Silva")
                        .email("carlos@gmail.com")
                        .age(28)
                        .address(new Address("Travessa da Alegria", "789", "54321-098"))
                        .build());

        authorRepository.saveAll(authors);

        // 1) Exemplo 1 - Usando Mapstruct para mapear de Author para AuthorDTO
        // Convertendo a lista de Author para AuthorDTO usando o AuthorMapper
        List<AuthorDTO> authorDTOs = AuthorMapper.INSTANCE.toDtoList(authors);

        System.out.println("\n Exemplo 1 - Usando Mapstruct para mapear de Author para AuthorDTO");

        // Imprimindo os dados dos AuthorDTOs
        authorDTOs.forEach(dto -> System.out.println(
                "Nome em formato UpperCase: " + dto.firstName() +
                " | Sobrenome: " + dto.lastName() +
                " | Email: " + dto.emailAddress() +
                " | Idade: " + dto.age() +
                " | Rua: " + dto.streetName() +
                " | Número: " + dto.houseNumber() +
                " | CEP: " + dto.zipCode()
        ));


        // 2) Exemplo 2 - Usando Mapstruct para mapear de AuthorDTO para Author

        AuthorDTO dto1 = new AuthorDTO("Ana", "Souza", "ana@gmail.com", 30, "Rua das Palmeiras", "321", "98765-432");          

        AuthorDTO dto2 = new AuthorDTO("Pedro", "Oliveira", "pedro@gmail.com", 40, "Avenida Central", "654", "12345-678");

        AuthorDTO dto3 = new AuthorDTO("Flávia", "Nunes", "flavia@gmail.com", 45, "Avenida das Américas", "456", "98990-889");

        // Convrtendo AuthorDTO para Author (entidade)
        Author author1 = AuthorMapper.INSTANCE.toEntity(dto1);
        Author author2 = AuthorMapper.INSTANCE.toEntity(dto2);
        Author author3 = AuthorMapper.INSTANCE.toEntity(dto3);

        System.out.println("\n Exemplo 2 - Usando Mapstruct para mapear de AuthorDTO para Author");

        // Imprimindo os dados dos autores convertidos
        for (Author author : List.of(author1, author2, author3)) {
            
            authorRepository.save(author);  // Salvando os autores convertidos no banco de dados

            System.out.println("Nome: " + author.getFirstName()
                + " | Sobrenome: " + author.getLastName()
                + " | Idade: " + author.getAge()
                + " | Email: " + author.getEmail()
                + " | Rua: " + author.getAddress().getStreetName()
                + " | Número: " + author.getAddress().getHouseNumber()
                + " | CEP: " + author.getAddress().getZipCode());
        }
    }

}
