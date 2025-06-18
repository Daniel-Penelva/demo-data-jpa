package com.api.demo_data_jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.api.demo_data_jpa.model.File;
import com.api.demo_data_jpa.model.Resource;
import com.api.demo_data_jpa.model.Text;
import com.api.demo_data_jpa.model.Video;
import com.api.demo_data_jpa.repository.FileRepository;
import com.api.demo_data_jpa.repository.ResourceRepository;
import com.api.demo_data_jpa.repository.TextRepository;
import com.api.demo_data_jpa.repository.VideoRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
public class InheritanceClassExample implements CommandLineRunner {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private TextRepository textRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var video = Video.builder()
                .name("Video 1")
                .size(15)
                .url("video1.com")
                .length(5)
                .build();

        var file = File.builder()
                .name("File 1")
                .size(5)
                .url("file1.com")
                .type("png")
                .build();

        var text = Text.builder()
                .name("Text 1")
                .size(10)
                .url("text1.com")
                .content("Este é um arquivo de texto.")
                .build();

        videoRepository.save(video);
        fileRepository.save(file);
        textRepository.save(text);

        System.out.println("Nome do vídeo: " + video.getName() + " - Duração: " + video.getLength());
        System.out.println("Nome do Arquivo: " + file.getName() + " - Tipo: " + file.getType());
        System.out.println("Nome do Arquivo: " + text.getName() + " - Conteúdo: " + text.getContent());

        
        // Exemplo utilizando Polimorfismo no Hibernate

        // 1) Consulta Polimórfica (EXPLICIT): Isso não traz as subclasses
        System.out.println("\n=== Consulta Resource ===");
        List<Resource> resources = resourceRepository.findAll();
        resources.forEach(System.out::println);  // Vai trazer somente Resource se existir - não traz Video, File e Text!


        // (2) Consulta explícita usando JPQL + TREAT
        System.out.println("\n=== Consulta Polimórfica usando TREAT - CONSULTA VIDEO ===");
        List<Video> videos = entityManager.createQuery(
                "SELECT TREAT(r AS Video) FROM Resource r WHERE TYPE(r) = Video",
                Video.class
        ).getResultList();

        videos.forEach(System.out::println);


        // (3) Consulta explícita de teste com instanceof
        System.out.println("\n=== Consulta Polimórfica usando INSTANCEOF ===");
        List<Resource> resources2 = resourceRepository.findAll();
        
        for(Resource resource : resources2) {
                System.out.println("Id: " + resource.getId() + " | Name: " + resource.getName());

                // Polimorfismo qual é o tipo real?
                if (resource instanceof Video) {
                        System.out.println("É um vídeo! Length: " + ((Video) resource).getLength());  // Utilizando Cast Video para acessar o método getLength()
                } else if (resource instanceof File) {
                        System.out.println("É um File! Type: " + ((File) resource).getType());  // Utilizando Cast File para acessar o método getType()
                } else if (resource instanceof Text) {
                        System.out.println("É um Text! Content: " + ((Text) resource).getContent());  // Utilizando Text para acessar o método getContent()
                } else {
                        System.out.println("Tipo desconhecido!");
                }
                System.out.println("-----------------------------------");
        }

    }

}
