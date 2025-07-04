package com.api.demo_data_jpa.specification;

import org.springframework.data.jpa.domain.Specification;

import com.api.demo_data_jpa.filter.AuthorBookFilter;
import com.api.demo_data_jpa.filter.AuthorFilter;
import com.api.demo_data_jpa.model.Author;
import com.api.demo_data_jpa.model.Book;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public interface AuthorSpecifications {

    // Define métodos estáticos para construir especificações de pesquisa para a entidade Author

    // Método para verificar se o primeiro nome é igual a um valor específico
    public static Specification<Author> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> firstName == null ? null : criteriaBuilder.equal(root.get("firstName"), firstName);
    }

    // Método para verificar se a idade é maior que um valor específico
    public static Specification<Author> hasAgeGreaterThan(int age) {
        return (root, query, criteriaBuilder) -> age <= 0 ? null : criteriaBuilder.greaterThan(root.get("age"), age);
    }

    // Método para verificar se o email contém um fragmento específico
    // O fragmento é convertido para minúsculas para garantir que a busca seja case-ins
    public static Specification<Author> emailsContains(String fragment) {
        return (root, query, criteriaBuilder) -> fragment == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + fragment.toLowerCase() + "%");
        
    }


    // Método para construir uma Specification baseada em um AuthorFilter
    // O AuthorFilter é um DTO que contém os critérios de filtro para a pesquisa.
    public static Specification<Author> build(AuthorFilter filter) {
        return Specification
        .where(firstNameLike(filter.getFirstName()))
        .and(emailsContains(filter.getEmailFragment()))
        .and(null != ageGreaterThanOrEqual(filter.getMinAge()) ? ageGreaterThanOrEqual(filter.getMinAge()) : null)
        .and(null != ageLessThanOrEqual(filter.getMaxAge()) ? ageLessThanOrEqual(filter.getMaxAge()) : null);
    } 

    // Método para verificar se o primeiro nome é igual a um valor específico
    private static Specification<Author> firstNameLike(String firstname) {
        return (root, query, criteriaBuilder) -> firstname != null && !firstname.isEmpty() ? 
            criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstname.toLowerCase() + "%") : null;
    }

    // Método para verificar se a idade é maior ou igual a um valor específico
    private static Specification<Author> ageGreaterThanOrEqual(Integer age) {
        return (root, query, criteriaBuilder) -> age != null && age > 0 ? 
            criteriaBuilder.greaterThanOrEqualTo(root.get("age"), age) : null;
    }

    // Método para verificar se a idade é menor ou igual a um valor específico
    private static Specification<Author> ageLessThanOrEqual(Integer age) {
        return (root, query, criteriaBuilder) -> age != null && age > 0 ? 
            criteriaBuilder.lessThanOrEqualTo(root.get("age"), age) : null;
    }


    // Método para construir uma Specification baseada em um AuthorBookFilter

    // O AuthorBookFilter é um DTO que contém os critérios de filtro para a pesquisa de autores e livros.
    public static Specification<Author> buildBookFilter(AuthorBookFilter filter) {
        return Specification
        .where(hasFirstName(filter.getFirstName()))
        .and(null != filter.getMinAge() ? hasAgeGreaterThan(filter.getMinAge()) : null)
        .and(bookTitleContains(filter.getBookTitleFragment()));
    }


    // Método para verificar se o titulo do livro contém um fragmento específico
    // O fragmento é convertido para minúsculas para garantir que a busca seja case-ins
    private static Specification<Author> bookTitleContains(String titleFragment) {
        return (root, query, criteriaBuilder) -> {
            if (titleFragment == null || titleFragment.isEmpty()) {
                return null;
            }
            
            root.fetch("books", JoinType.LEFT);  // Faz o join com a entidade Book
            query.distinct(true);  // Garante que os resultados sejam distintos
            Join<Author, Book> join = root.join("books", JoinType.LEFT); // Faz o join com a entidade Book
            return criteriaBuilder.like(criteriaBuilder.lower(join.get("title")), "%" + titleFragment.toLowerCase() + "%");

        };
    }

}
