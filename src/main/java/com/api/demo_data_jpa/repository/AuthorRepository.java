package com.api.demo_data_jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.demo_data_jpa.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer>{

    // SQL: SELECT * FROM author WHERE email = 'daniel@gmail.com'
    // JPQL: SELECT a FROM Author a WHERE a.email = :email
    // Buscar un autor por email
    Author findByEmail(String email);

    // SQL: SELECT * FROM author WHERE first_name = "Daniel";
    // JPQL: SELECT a FROM Author a WHERE a.first_name = :first_name
    // Equivalente a List<Author> findByFirstName(String firstName);
    // Buscar por nome exato
    List<Author> findAllByFirstName(String firstName);

    // SQL: SELECT * FROM author WHERE first_name = "Daniel"
    // JPQL: SELECT a FROM Author a WHERE a.first_name = :first_name
    // Buscar por nome exato ignorando letras maiúsculas e minúsculas.
    List<Author> findByFirstNameIgnoreCase(String firstName);

    // SQL: SELECT * FROM author WHERE first_name = "Daniel"
    // JPQL: SELECT a FROM Author a WHERE a.first_name = :keyword
    // Buscar por nome contendo o nome ignorando letras maiúsculas e minúsculas - utiliza o operador LiKE '%Dan%'. 
    List<Author> findByFirstNameContainingIgnoreCase(String keyword);

    // SQL: SELECT * FROM author WHERE first_name LIKE 'Dan%'
    // JPQL: SELECT a FROM Author a WHERE a.first_name LIKE :keyword
    // Buscar por todos com nome começando com a palavra "Dan" (LiKE 'Dan%') ignorando letras maiúsculas e minúsculas.
    List<Author> findByFirstNameStartsWithIgnoreCase(String prefix);

    // SQL: SELECT * FROM author WHERE first_name LIKE '%Dan'
    // JPQL: SELECT a FROM Author a WHERE a.first_name LIKE :prefix 
    // Buscar por todos com nome terminando com a palavra "Dan" (LiKE '%Dan') ignorando letras maiúsculas e minúsculas.
    List<Author> findByFirstNameEndsWithIgnoreCase(String prefix);

    // SQL: SELECT * FROM Author WHERE first_name in('daniel', 'ped', 'marc')
    // JPQL: SELECT a FROM Author a WHERE a.first_name IN (:firstNames)
    // Buscar por nome exato utilizando uma lista de valores 
    List<Author> findByFirstNameInIgnoreCase(List<String> firstNames);

    // SQL: SELECT * FROM Author WHERE first_name = "Daniel" OR last_name = "Penelva";
    // JPQL: SELECT a FROM Author a WHERE a.first_name = :first_name OR a.last_name = :last_name;
    // Buscar por nome exato ou por sobrenome exato ignorando letras maiúsculas e minúsculas.
    List<Author> findByFirstNameOrLastNameIgnoreCase(String firstname, String lastname);

    // SQL: SELECT * FROM Author WHERE age > 30;
    // JPQL: SELECT a FROM Author a WHERE a.age > :age;
    // Buscar por idade maior que...
    List<Author> findByAgeGreaterThan(int age);
    
    // SQL: SELECT * FROM Author WHERE age BETWEEN 30 AND 40;
    // JPQL: SELECT a FROM Author a WHERE a.age BETWEEN :start AND :end;
    // Buscar por idade entre...
    List<Author> findByAgeBetween(int start, int end);

    // SQL: SELECT * FROM author WHERE email = "gmail"
    // JPQL: SELECT a FROM Author a WHERE a.email LIKE :keyword
    // Buscar por email contendo...
    List<Author> findByEmailContaining(String keyword);

    // SQL: SELECT * FROM author WHERE EXISTS (SELECT email FROM author WHERE email = "gmail")
    // JPQL: SELECT a FROM Author a WHERE EXISTS (SELECT b FROM Author b WHERE b .email = :email)
    // Verificar se existe autor com email.
    boolean existsByEmail(String email);

    // SQL: SELECT COUNT(age) FROM author
    // JPQL: SELECT COUNT(a.age) FROM Author a
    // Contar autores com idade maior que...
    long countByAgeGreaterThan(int age);

    // SQL: SELECT COUNT(age) FROM author
    // JPQL: SELECT COUNT(a.age) FROM Author a
    // Contar autores com idade maior ou igual a...
    long countByAgeGreaterThanEqual(int age);

    // Buscar todos com o nome começando com...
    // SQL: SELECT * FROM author WHERE first_name LIKE 'Dan%'
    // JPQL: SELECT a FROM Author a WHERE a.first_name LIKE :prefix
    // Buscar todos com o nome começando com...
    List<Author> findAllByFirstNameStartingWith(String prefix);
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
 * Consultas Derivadas: 
 *   - No Spring Data JPA, consultas derivadas são métodos do repositório que o Spring entende e transforma automaticamente em SQL/JPQL baseado no nome do método.
 *   - Sintaxe Básica:
 *      => findBy = retorna 1 ou mais registros que atendem à condição.
 *      => findAllBy =  igual ao findBy, mas semântico: enfatiza que espera vários resultados.
 *      => countBy = retorna um número (quantos registros atendem à condição).
 *      => existsBy = retorna true se existir pelo menos 1 registro que atende à condição. Ou seja, retorna true/false se existir ou não.
 * 
*/