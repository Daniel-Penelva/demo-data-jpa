package com.api.demo_data_jpa.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.api.demo_data_jpa.model.Author;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
public class AuthorLifecycleExample implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // 1. TRANSIENT STATE
        Author author = new Author();  // novo objeto, sem ID
        author.setFirstName("Daniel");
        author.setLastName("Penelva");
        author.setEmail("daniel.p@gmail.com");
        author.setAge(37);


        // 2. PERSISTENT (gerenciado pelo Hbiernate)
        entityManager.persist(author); 
        System.out.println("Persistido: " + author.getId()); // autor agora está no estado persistente, ou seja, está salvo no banco de dados com ID gerado 

        
        // 3. DETACHED (não gerenciado pelo Hibernate)
        entityManager.detach(author); // autor agora está no estado detached, ou seja, não está mais sendo gerenciado pelo Hibernate - remove o objeto do contexto de persistência
        author.setFirstName("Daniel Updated"); // alteração não será refletida no banco de dados, pois o autor está detached


        // 4. Merge (volta para a persistência - reativa o objeto no contexto de persistência)
        Author mergedAuthor = entityManager.merge(author); // cria uma cópia gerenciada - o autor é reativado no contexto de persistência, e agora as alterações serão refletidas no banco de dados


        // 5. REMOVED (removendo o autor do banco de dados)
        entityManager.remove(mergedAuthor); // o autor é removido do contexto de persistência
        System.out.println("Autor removido com ID: " + mergedAuthor.getId());

        // Após o commit da transação, o autor será excluído do banco de dados
        System.out.println("Transação concluída. Autor removido do banco de dados.");
    }
    
}
