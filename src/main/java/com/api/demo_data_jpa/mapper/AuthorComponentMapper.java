package com.api.demo_data_jpa.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.api.demo_data_jpa.dto.AuthorDTO;
import com.api.demo_data_jpa.model.Author;

@Component
public class AuthorComponentMapper {

    // Converte um Author para AuthorDTO
    public AuthorDTO toDTO(Author author) {
        if (author == null) {
            return null;
        }
        return new AuthorDTO(
                author.getFirstName(),
                author.getLastName(),
                author.getEmail(),
                author.getAge());
    }


    // Converte uma lista de Author para uma lista de AuthorDTO
    public List<AuthorDTO> toDTOList(List<Author> authors) {
        if (authors == null || authors.isEmpty()) {
            return List.of();
        }
        return authors.stream()
                .map(this::toDTO)
                .toList();
    }
}
