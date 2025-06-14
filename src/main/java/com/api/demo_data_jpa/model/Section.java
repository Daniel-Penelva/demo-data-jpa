package com.api.demo_data_jpa.model;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "SECTION_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Section extends BaseEntity{

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
    // Quando um curso for deletado, todas as seções associadas a ele também serão deletadas.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", foreignKey = @ForeignKey(name = "fk_section_course_id"))
    @OnDelete(action = OnDeleteAction.CASCADE) 
    @JsonIgnore
    private Course course;

    // Bom Saber: A classe que possuir @JoinColumn é o lado dono (Lecture). A classe que possuir o mappedBy é o lado inverso (Section).
    // O Lecture é o relacionamento principal, é o lado do dono da relação e o Section é o lado dependente da relação.
    // mappedBy = "section" aqui, o Section apenas aponta para o campo section da entidade Lecture, apenas para mapear a relação.
    // A relação é OneToMany, então uma seção pode ter várias palestras, mas cada palestra pertence a uma única seção.
    // Como se lê: Uma seção pode ter várias palestras, mas cada palestra pertence a uma única seção.
    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY)
    @JsonIgnore
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
 * 
 * @OnDelete: Define o comportamento de exclusão em cascata para a relação. Quando uma entidade é deletada, as entidades relacionadas também serão deletadas.
 *      - action: Define a ação a ser executada quando a entidade principal for deletada (ex: CASCADE, SET_NULL, etc.).
 *      - CASCADE: Quando a entidade principal for deletada, todas as entidades relacionadas também serão deletadas.
 *      - SET_NULL: Quando a entidade principal for deletada, a relação será nula.
 * 
 * fetch = FetchType.LAZY: Define o tipo de carregamento da relação. O LAZY significa que os dados serão carregados somente quando necessário, ou seja, quando a coleção for acessada.
 * fetch = FetchType.EAGER: Define o tipo de carregamento da relação. O EAGER significa que os dados serão carregados imediatamente, ou seja, quando a entidade for carregada, as coleções relacionadas também serão carregadas.
 * @JsonIgnore: Anotação do Jackson que indica que o campo deve ser ignorado durante a serialização e desserialização JSON. Isso é útil para evitar loops infinitos em relações bidirecionais.
 * @JsonProperty: Anotação do Jackson que indica que o campo deve ser serializado e desserializado com um nome específico no JSON. Isso é útil para personalizar o nome do campo no JSON.
 * optional = false: Indica que a relação é obrigatória, ou seja, não pode ser nula. (chave estrangeira obrigatória)
 * 
*/

/* Observação: 
 *
 * Vale para todos os relacionamentos: 
 *  - A classe que possuir @JoinColumn e @JoinTable é o lado dono. 
 *  - A classe que possuir o mappedBy é o lado inverso.
 * */  