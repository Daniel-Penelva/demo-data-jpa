package com.api.demo_data_jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "BOOK_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class Book extends BaseEntity{

    private String title;

    // Muitos livros podem ter o mesmo autor, mas um livro pertence a um único autor.
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    
}
