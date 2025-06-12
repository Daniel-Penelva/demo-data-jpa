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

    // O "Many" representa a entidade dependente (Section) e o "One" representa a entidade principal (Course).
    // O curso é o dono da relação, pois ele tem a anotação @OneToMany(mappedBy = "course") na classe Course.
    // A anotação @JoinColumn é usada para especificar a coluna que será usada como chave estrangeira na tabela de seções.
    // A coluna "course_id" é a chave estrangeira que referencia a tabela de cursos.
    // O foreignKey é usado para definir a chave estrangeira com um nome específico.
    // A relação é ManyToOne, então várias seções podem pertencer a um único curso, mas cada seção pertence a um único curso.
    // Como se lê: Várias seções podem pertencer a um único curso, mas cada seção pertence a um único curso.
    @ManyToOne
    @JoinColumn(name = "course_id", foreignKey = @ForeignKey(name = "fk_section_course_id"))
    private Course course;

    // "One" representa a entidade principal (Section) e "Many" representa a entidade dependente (Lecture).
    // A seção é o dono da relação, pois ela tem a anotação @OneToMany(mappedBy = "section") na classe Section.
    // O lecture é o inverso da relação, pois ele tem a anotação @ManyToOne.
    // O "mappedBy" indica que a coleção de lectures é mapeada pela propriedade "section" na classe Lecture.
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