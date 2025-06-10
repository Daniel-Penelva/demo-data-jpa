package com.api.demo_data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.demo_data_jpa.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer>{

    Author findByEmail(String email);
    
}

/*Anotação:
 * @Repository: Indica que esta interface é um repositório Spring, permitindo que o Spring Data JPA forneça a implementação padrão para operações CRUD.
 * JpaRepository<Author, Integer>: Extende a interface JpaRepository, que fornece métodos para operações CRUD e consultas personalizadas. O primeiro parâmetro é a entidade (Author) e o segundo é o tipo do identificador (Integer).
 * Essa interface não precisa de implementação, pois o Spring Data JPA gera automaticamente a implementação em tempo de execução com base nas convenções de nomenclatura dos métodos.
 * 
 *  - flush(): Método que sincroniza o estado do contexto de persistência com o banco de dados, garantindo que todas as alterações pendentes sejam escritas.
 *  - save(Author author): Método que salva ou atualiza uma entidade Author no banco de dados. Se o autor já existir (com base no ID), ele será atualizado; caso contrário, será criado um novo registro.
 *  - findById(Integer id): Método que busca um autor pelo seu ID. Retorna um Optional<Author>, que pode conter o autor encontrado ou estar vazio se não houver correspondência.
 *  - deleteById(Integer id): Método que remove um autor do banco de dados com base no seu ID. Se o autor não existir, não fará nada.
 *  - delete(Author author): Método que remove um autor específico do banco de dados. Se o autor não existir, não fará nada.
 *  - findAll(): Método que retorna uma lista de todos os autores presentes no banco de dados. É útil para obter todos os registros da tabela Author.
 *  - findAllById(Iterable<Integer> ids): Método que busca todos os autores cujos IDs estão presentes no iterable fornecido. Retorna uma lista de autores correspondentes.
 *  - saveAll(Iterable<Author> authors): Método que salva ou atualiza uma coleção de entidades Author no banco de dados. Se um autor já existir (com base no ID), ele será atualizado; caso contrário, novos registros serão criados.
 *  - count(): Método que retorna o número total de autores no banco de dados. É útil para obter uma contagem rápida dos registros.
 *  - existsById(Integer id): Método que verifica se um autor com o ID especificado existe no banco de dados. Retorna true se existir, false caso contrário.
 *  - findByName(String name): Método que busca autores pelo nome. Retorna uma lista de autores cujo nome corresponde ao parâmetro fornecido. É útil para consultas personalizadas baseadas em atributos específicos.
 * Observação:
 * Esses métodos são herdados da interface JpaRepository e podem ser usados diretamente sem necessidade de implementação adicional.
 * 
 * Interfaces:
 *  Repository: Interface base do Spring Data JPA que define métodos comuns para operações de persistência.
 *  PagingAndSortingRepository: Interface que estende Repository e adiciona suporte para paginação e ordenação de resultados.
 *  CrudRepository: Interface que estende Repository e fornece métodos CRUD básicos.
 *  ListPagingAndSortingRepository: Interface que estende PagingAndSortingRepository e adiciona suporte para operações de listagem.
 *  ListCrudRepository: Interface que estende CrudRepository e adiciona suporte para operações de listagem.
 *  JpaRepository: Interface que estende PagingAndSortingRepository e CrudRepository, fornecendo uma ampla gama de métodos para operações de persistência avançadas.
 * 
*/