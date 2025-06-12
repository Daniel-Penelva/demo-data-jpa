package com.api.demo_data_jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LECTURE_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String name;

    // Bom Saber: A classe que possuir @JoinColumn é o lado dono (Lecture). A classe que possuir o mappedBy é o lado inverso (Resource).
    // O Lecture é o relacionamento principal, é o lado do dono da relação e o Section é o lado dependente da relação.
    // A anotação @JoinColumn é usada para especificar a coluna que será usada como chave estrangeira na tabela de palestras.
    // A coluna "section_id" é a chave estrangeira que referencia a tabela de seções.
    // O foreignKey é usado para definir a chave estrangeira com um nome específico.
    // A relação é ManyToOne, então várias palestras podem pertencer a uma única seção, mas cada palestra pertence a uma única seção.
    // Como se lê: Várias palestras podem pertencer a uma única seção, mas cada palestra pertence a uma única seção.
    @ManyToOne
    @JoinColumn(name = "section_id", foreignKey = @ForeignKey(name = "fk_lecture_section_id"))
    private Section section;

    // É uma relação UNIDIRECIONAL OneToOne entre Lecture e Resource - para ser unidirecional não usa a propriedade mappedBy - aqui, para acessar o Resource, você precisa acessar a Lecture.
    // Bom Saber: A classe que possuir @JoinColumn é o lado dono (Lecture). A classe que possuir o mappedBy é o lado inverso (Resource).
    // O Lecture é o relacionamento principal, é o lado do dono da relação e o Resource é o lado dependente da relação.
    // A anotação @JoinColumn é usada para especificar a coluna que será usada como chave estrangeira na tabela de recursos.
    // A coluna "resource_id" é a chave estrangeira que referencia a tabela de recursos.
    // O foreignKey é usado para definir a chave estrangeira com um nome específico.
    // A relação é OneToOne, então uma palestra pode ter um único recurso, e um recurso pode pertencer a uma única palestra.
    // Como se lê: Uma palestra pode ter um único recurso, e um recurso pode pertencer a uma única palestra.
    @OneToOne
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_lecture_resource_id"))
    private Resource resource;
    
}

/*Anotação: 
 * @OneToOne: Define uma relação unidirecional ou bidirecional (utiliza o mappedBy) OneToOne entre duas entidades, onde uma entidade possui exatamente uma instância da outra.
 * @JoinColumn: Especifica a coluna que será usada como chave estrangeira na tabela da entidade que possui a relação.
 * @ForeignKey: Define a chave estrangeira com um nome específico, que pode ser útil para manter a integridade referencial no banco de dados.
 * 
 * Para ser uma relação unidirecional, a entidade principal não deve ter a relação mappedBy, e a entidade dependente deve ter a anotação @JoinColumn para especificar a coluna de junção.
*/

/* Observação: 
 *
 * Vale para todos os relacionamentos: 
 *  - A classe que possuir @JoinColumn e @JoinTable é o lado dono. 
 *  - A classe que possuir o mappedBy é o lado inverso.
 * */  
