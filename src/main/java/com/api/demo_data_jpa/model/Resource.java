package com.api.demo_data_jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RESOURCE_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String name;

    private int size;
    private String url;

    // É uma relação BIDIRECIONAL OneToOne entre Resource e Lecture - para ser bidirecional usa a propriedade mappedBy na classe Lecture. Aqui, para acessar a Lecture, você pode acessar o Resource diretamente através da propriedade lecture ou acessar a Lecture através do Resource, pois a relação é bidirecional. 
    // O "One" representa a entidade principal (Resource) e "One" representa a entidade dependente (Lecture).
    // O recurso (Resource) é o dono da relação, pois ela tem a anotação @OneToOne na classe Palestra (Lecture).
    // mappedBy = "resource" indica que a propriedade resource na classe Lecture é responsável por mapear essa relação.
    // A relação é OneToOne, então um recurso pode pertencer a uma única palestra, e uma palestra pode ter um único recurso.
    // Como se lê: Um recurso pode pertencer a uma única palestra, e uma palestra pode ter um único recurso.
    @OneToOne(mappedBy = "resource")
    private Lecture lecture;
    
}
