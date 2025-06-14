package com.api.demo_data_jpa.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "LECTURE_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Lecture extends BaseEntity{

    @Column(length = 100)
    private String name;

    // Bom Saber: A classe que possuir @JoinColumn é o lado dono (Lecture). A classe que possuir o mappedBy é o lado inverso (Resource).
    // O Lecture é o relacionamento principal, é o lado do dono da relação e o Section é o lado dependente da relação.
    // A anotação @JoinColumn é usada para especificar a coluna que será usada como chave estrangeira na tabela de palestras.
    // A coluna "section_id" é a chave estrangeira que referencia a tabela de seções.
    // O foreignKey é usado para definir a chave estrangeira com um nome específico.
    // A relação é ManyToOne, então várias palestras podem pertencer a uma única seção, mas cada palestra pertence a uma única seção.
    // Como se lê: Várias palestras podem pertencer a uma única seção, mas cada palestra pertence a uma única seção.
    // Quando uma seção for deletada, todas as palestras associadas a ela também serão deletadas.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "section_id", foreignKey = @ForeignKey(name = "fk_lecture_section_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Section section;

    // É uma relação UNIDIRECIONAL OneToOne entre Lecture e Resource - para ser unidirecional não usa a propriedade mappedBy - aqui, para acessar o Resource, você precisa acessar a Lecture.
    // Bom Saber: A classe que possuir @JoinColumn é o lado dono (Lecture). A classe que possuir o mappedBy é o lado inverso (Resource).
    // O Lecture é o relacionamento principal, é o lado do dono da relação e o Resource é o lado dependente da relação.
    // A anotação @JoinColumn é usada para especificar a coluna que será usada como chave estrangeira na tabela de recursos.
    // A coluna "resource_id" é a chave estrangeira que referencia a tabela de recursos.
    // O foreignKey é usado para definir a chave estrangeira com um nome específico.
    // A relação é OneToOne, então uma palestra pode ter um único recurso, e um recurso pode pertencer a uma única palestra.
    // Como se lê: Uma palestra pode ter um único recurso, e um recurso pode pertencer a uma única palestra.
    // Quando uma palestra for deletada, o recurso associado a ela também será deletado.
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_lecture_resource_id"))
    @OnDelete(action = OnDeleteAction.CASCADE) 
    private Resource resource;
    
}

/*Anotação: 
 * @OneToOne: Define uma relação unidirecional ou bidirecional (utiliza o mappedBy) OneToOne entre duas entidades, onde uma entidade possui exatamente uma instância da outra.
 * @JoinColumn: Especifica a coluna que será usada como chave estrangeira na tabela da entidade que possui a relação.
 * @ForeignKey: Define a chave estrangeira com um nome específico, que pode ser útil para manter a integridade referencial no banco de dados.
 * 
 * Para ser uma relação unidirecional, a entidade principal não deve ter a relação mappedBy, e a entidade dependente deve ter a anotação @JoinColumn para especificar a coluna de junção.
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
*/

/* Observação: 
 *
 * Vale para todos os relacionamentos: 
 *  - A classe que possuir @JoinColumn e @JoinTable é o lado dono. 
 *  - A classe que possuir o mappedBy é o lado inverso.
 * */  
