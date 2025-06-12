package com.api.demo_data_jpa.model;

import java.util.List;

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

    // Definindo a tabela de junção e é definido do lado do proprietário da relação, no caso o Course.
    // O Course é o dono da relação, pois ele tem a anotação @ManyToMany e a tabela de junção.
    // O Author é o inverso da relação, pois ele tem a anotação @ManyToMany(mappedBy = "authors").
    // A tabela de junção é criada com o nome "courses_authors" e as colunas "course_id" e "author_id".
    // A coluna "course_id" é a chave estrangeira que referencia a tabela de cursos e a coluna "author_id" é a chave estrangeira que referencia a tabela de autores.
    // A relação é ManyToMany, então o curso pode ter vários autores e o autor pode ter vários cursos.
    @ManyToMany
    @JoinTable(
        name = "courses_authors",
        joinColumns = { @JoinColumn(name = "course_id") },
        inverseJoinColumns = { @JoinColumn(name = "author_id") }
    )
    List<Author> authors;

    // "One" representa a entidade principal (Course) e "Many" representa a entidade dependente (Section).
    // O Course é o dono da relação, pois ele tem a anotação @OneToMany(mappedBy = "course") na classe Course.
    // A section é o inverso da relação, pois ela tem a anotação @ManyToOne.
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
*/

/* Observação: 
 *
 * @OneToMany e @ManyToOne – Quem é quem?
 * @OneToMany:
 *   - "One" => é a entidade principal que possui várias instâncias da outra.
 *   - "Many" => é a entidade dependente ou secundária que pertence a uma única instância da entidade principal.
 * 
 * @ManyToOne:
 *   - "Many" => é a entidade dependente ou secundária que pode pertencer a várias instâncias da entidade principal.
 *   - "One" => é a entidade principal que pode ter várias instâncias da entidade dependente.
 * 
 * Em resumo:
 * O "One" representa o lado principal da relação (proprietário), enquanto o "Many" representa o lado dependente.
 * O dono da chave estrangeira é sempre o lado "Many" da relação, que possui a coluna de chave estrangeira referenciando o lado "One".
 * A entidade que contém o "mappedBy" em @OneToMany é a inversa da relação é a entidade que não possui a chave estrangeira e, portanto, não é o dono da relação.
 * */  