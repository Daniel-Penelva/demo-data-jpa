package com.api.demo_data_jpa.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorFilter {

    // DTO de filtro para Author
    private String firstName;
    private String emailFragment;
    private Integer minAge;
    private Integer maxAge;

}
