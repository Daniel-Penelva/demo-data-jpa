package com.api.demo_data_jpa.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.api.demo_data_jpa.dto.AuthorDTO;
import com.api.demo_data_jpa.model.Author;


@Mapper
public interface AuthorMapper {

    // Este é um exemplo de como criar um mapper com MapStruct
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    // 1) ENTIDADE -> DTO
    // Converte uma entidade Author em um DTO AuthorDTO
    @Mappings({ 
        @Mapping(source = "email", target = "emailAddress"),
        @Mapping(source = "address.streetName", target = "streetName"),
        @Mapping(source = "address.houseNumber", target = "houseNumber"),
        @Mapping(source = "address.zipCode", target = "zipCode"),
        @Mapping(source = "firstName", target = "firstName", qualifiedByName = "toUpper")
    })
    AuthorDTO toDto(Author author);

    // Converte uma lista de entidades Author em uma lista de DTOs AuthorDTO
    List<AuthorDTO> toDtoList(List<Author> authors);

    @Named("toUpper")
    static String toUpperCase(String value) {
        return value != null ? value.toUpperCase() : null;
    }

    // 2) DTO -> ENTIDADE
    // Converte um DTO AuthorDTO em uma entidade Author
    @InheritInverseConfiguration  // Inverte o mapeamento do método toDto 
    Author toEntity(AuthorDTO authorDTO);

    List<Author> toEntitList(List<AuthorDTO> authorDTOs);

}
