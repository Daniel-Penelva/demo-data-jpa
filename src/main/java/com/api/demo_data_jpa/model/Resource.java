package com.api.demo_data_jpa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "RESOURCE_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Resource extends BaseEntity{

    @Column(length = 100)
    private String name;

    private int size;
    private String url;

    // É uma relação BIDIRECIONAL OneToOne entre Resource e Lecture - para ser bidirecional usa a propriedade mappedBy na classe Lecture. Aqui, para acessar a Lecture, você pode acessar o Resource diretamente através da propriedade lecture ou acessar a Lecture através do Resource, pois a relação é bidirecional. 
    // Bom Saber: A classe que possuir @JoinColumn é o lado dono (Lecture). A classe que possuir o mappedBy é o lado inverso (Resource).
    // Lecture é o relacionamento principal, é o lado do dono da relação e o Resource é o lado inverso da relação.
    // mappedBy = "resource" aqui, o Resource apenas aponta para o campo resource da entidade Lecture, apenas para mapear a relação.
    // A relação é OneToOne, então um recurso pode pertencer a uma única palestra, e uma palestra pode ter um único recurso.
    // Como se lê: Um recurso pode pertencer a uma única palestra, e uma palestra pode ter um único recurso.
    @OneToOne(mappedBy = "resource", fetch = FetchType.LAZY)
    @JsonIgnore
    private Lecture lecture;
    
}

/* Observação: 
 *
 * Vale para todos os relacionamentos: 
 *  - A classe que possuir @JoinColumn e @JoinTable é o lado dono. 
 *  - A classe que possuir o mappedBy é o lado inverso.
 * */  
