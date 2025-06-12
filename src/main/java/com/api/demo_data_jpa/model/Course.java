package com.api.demo_data_jpa.model;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "COURSE_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    // Bom Saber: A classe que possuir @JoinColumn e @JoinTable é o lado dono (Course). A classe que possuir o mappedBy é o lado inverso (Author).
    // O Course é o relacionamento principal, é o lado do dono da relação e o Author é o lado dependente da relação.
    // A tabela de junção é criada com o nome "courses_authors" e as colunas "course_id" e "author_id".
    // A coluna "course_id" é a chave estrangeira que referencia a tabela de cursos e a coluna "author_id" é a chave estrangeira que referencia a tabela de autores.
    // A relação é ManyToMany, então o curso pode ter vários autores e o autor pode ter vários cursos.
    // Como se lê: Um curso pode ter vários autores.
    // Quando um curso for deletado, todos os autores associados a ele também serão deletados.
    @ManyToMany
    @JoinTable(
        name = "courses_authors",
        joinColumns = { @JoinColumn(name = "course_id") },
        inverseJoinColumns = { @JoinColumn(name = "author_id") }
    )
    @OnDelete(action = OnDeleteAction.CASCADE) 
    List<Author> authors;

    // Bom Saber: A classe que possuir @JoinColumn é o lado dono (Section). A classe que possuir o mappedBy é o lado inverso (Course).
    // O Section é o relacionamento principal, é o lado do dono da relação e o Course é o lado dependente da relação.
    // mappedBy = "course" aqui, o Course apenas aponta para o campo course da entidade Section, apenas para mapear a relação.
    // A relação é OneToMany, então o curso pode ter várias seções, mas cada seção pertence a um único curso.
    // Como se lê: Um curso pode ter várias seções, mas cada seção pertence a um único curso.
    @OneToMany(mappedBy = "course")
    List<Section> sections;
    
}

/* Anotação:
 * @manyToMany: Define uma relação ManyToMany entre entidades.
 *      - mappedBy: Indica o lado inverso da relação. Vai ser usado na entidade que não é o dono da relação. Vai mapear a coleção de entidades inversas.
 * @JoinTable: Define a tabela de junção para a relação ManyToMany.
 *      - name: Nome da tabela de junção.
 *      - joinColumns: Define as colunas de junção para a entidade atual.
 *      - inverseJoinColumns: Define as colunas de junção para a entidade inversa.
 * @JoinColumn: Define uma coluna de junção para a relação.
 *      - name: Nome da coluna de junção.
 *      - referencedColumnName: Nome da coluna referenciada na entidade inversa.
 *      - nullable: Indica se a coluna de junção pode ser nula.
 *      - unique: Indica se a coluna de junção deve ser única.
 *      - insertable: Indica se a coluna de junção pode ser incluída em operações de inserção.
 *      - updatable: Indica se a coluna de junção pode ser atualizada.
 *      - foreignKey: Define a chave estrangeira para a coluna de junção.
 * 
 * @OneToMany: Define uma relação OneToMany entre entidades. O "One" representa a entidade principal (One) e o "Many" representa a entidade dependente (Many).
 *      - mappedBy: Indica o lado inverso da relação. Vai ser usado na entidade que não é o dono da relação. Vai mapear a coleção de entidades inversas.
 * @ManyToOne: Define uma relação ManyToOne entre entidades. O "One" representa a entidade principal (One) e o "Many" representa a entidade dependente (Many).
 *      - fetch: Define o tipo de carregamento da relação (EAGER ou LAZY).
 *      - optional: Indica se a relação é opcional (pode ser nula).
 * @JoinColumn: Define uma coluna de junção para a relação.
 *      - name: Nome da coluna de junção.
 *      - referencedColumnName: Nome da coluna referenciada na entidade inversa.
 *      - nullable: Indica se a coluna de junção pode ser nula.
 *      - unique: Indica se a coluna de junção deve ser única.
 *      - insertable: Indica se a coluna de junção pode ser incluída em operações de inserção.
 *      - updatable: Indica se a coluna de junção pode ser atualizada.
 *      - foreignKey: Define a chave estrangeira para a coluna de junção.
 * 
 * @OnDelete: Define o comportamento de exclusão em cascata para a relação. Quando uma entidade é deletada, as entidades relacionadas também serão deletadas.
 *      - action: Define a ação a ser executada quando a entidade principal for deletada (ex: CASCADE, SET_NULL, etc.).
 *      - CASCADE: Quando a entidade principal for deletada, todas as entidades relacionadas também serão deletadas.
 *      - SET_NULL: Quando a entidade principal for deletada, a relação será nula.
 * 
*/

/* Observação: 
 *
 * Vale para todos os relacionamentos: 
 *  - A classe que possuir @JoinColumn e @JoinTable é o lado dono. 
 *  - A classe que possuir o mappedBy é o lado inverso.
 * */  