package com.api.demo_data_jpa.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate 
    private LocalDateTime createdAt; // Vai criar a data e a hora automaticamente quando o objeto for criado

    @LastModifiedDate
    private LocalDateTime lastModifiedAt; // Vai criar a data e na hora automaticamente quando o objeto for atualizado

    @CreatedBy
    private String createdBy; // Vai criar o usuário que criou o objeto

    @LastModifiedBy
    private String lastModifiedBy; // Vai criar o usuário que atualizou o objeto
    
}

/* Anotação:
 * @MappedSuperclass: Indica que esta classe é uma superclasse mapeada, e suas propriedades serão herdadas por outras entidades.
 * @EntityListeners: Permite que a classe seja escutada por eventos de persistência, como criação e atualização.
 * Classe AuditingEntityListener : É uma classe que escuta os eventos de persistência e atualiza as propriedades d
 * @CreatedDate: Cria a data e hora automaticamente quando o objeto é criado.
 * @LastModifiedDate: Cria a data e hora automaticamente quando o objeto é atualizado.
 * @CreatedBy: Cria o usuário que criou o objeto.
 * @LastModifiedBy: Cria o usuário que atualizou o objeto.
 * @SuperBuilder: Permite a construção de objetos usando o padrão Builder, facilitando a criação de instâncias da classe com propriedades opcionais.
*/
