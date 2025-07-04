package com.api.demo_data_jpa.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorBookFilter {

    private String firstName;
    private String bookTitleFragment;
    private Integer minAge;
    
}
