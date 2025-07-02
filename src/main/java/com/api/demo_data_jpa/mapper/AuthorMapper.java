package com.api.demo_data_jpa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.api.demo_data_jpa.dto.AuthorDTO;
import com.api.demo_data_jpa.model.Author;


@Mapper
public interface AuthorMapper {

    // Este é um exemplo de como criar um mapper com MapStruct
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    // 1) ENTIDADE -> DTO
    // Converte uma entidade Author em um DTO AuthorDTO
    AuthorDTO toDto(Author author);

    // Converte uma lista de entidades Author em uma lista de DTOs AuthorDTO
    List<AuthorDTO> toDtoList(List<Author> authors);
    
}
