package com.api.demo_data_jpa.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SECTION_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String name;

    @Column(name = "section_order")
    private int sectionOrder;

    // Bom Saber: A classe que possuir @JoinColumn é o lado dono (Section). A classe que possuir o mappedBy é o lado inverso (Course).
    // O Section é o relacionamento principal, é o lado do dono da relação e o Course é o lado dependente da relação.
    // A anotação @JoinColumn é usada para especificar a coluna que será usada como chave estrangeira na tabela de seções.
    // A coluna "course_id" é a chave estrangeira que referencia a tabela de cursos.
    // O foreignKey é usado para definir a chave estrangeira com um nome específico.
    // A relação é ManyToOne, então várias seções podem pertencer a um único curso, mas cada seção pertence a um único curso.
    // Como se lê: Várias seções podem pertencer a um único curso, mas cada seção pertence a um único curso.
    @ManyToOne
    @JoinColumn(name = "course_id", foreignKey = @ForeignKey(name = "fk_section_course_id"))
    private Course course;

    // Bom Saber: A classe que possuir @JoinColumn é o lado dono (Lecture). A classe que possuir o mappedBy é o lado inverso (Section).
    // O Lecture é o relacionamento principal, é o lado do dono da relação e o Section é o lado dependente da relação.
    // mappedBy = "section" aqui, o Section apenas aponta para o campo section da entidade Lecture, apenas para mapear a relação.
    // A relação é OneToMany, então uma seção pode ter várias palestras, mas cada palestra pertence a uma única seção.
    // Como se lê: Uma seção pode ter várias palestras, mas cada palestra pertence a uma única seção.
    @OneToMany(mappedBy = "section")
    private List<Lecture> lectures;
    
}

/*Anotação:
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
 * @OneToOne: Define uma relação unidirecional ou bidirecional (utiliza o mappedBy) OneToOne entre duas entidades, onde uma entidade possui exatamente uma instância da outra.
 * @JoinColumn: Especifica a coluna que será usada como chave estrangeira na tabela da entidade que possui a relação.
 * @ForeignKey: Define a chave estrangeira com um nome específico, que pode ser útil para manter a integridade referencial no banco de dados.
*/

/* Observação: 
 *
 * Vale para todos os relacionamentos: 
 *  - A classe que possuir @JoinColumn e @JoinTable é o lado dono. 
 *  - A classe que possuir o mappedBy é o lado inverso.
 * */  