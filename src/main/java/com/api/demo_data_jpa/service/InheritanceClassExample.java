package com.api.demo_data_jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.api.demo_data_jpa.model.File;
import com.api.demo_data_jpa.model.Text;
import com.api.demo_data_jpa.model.Video;
import com.api.demo_data_jpa.repository.FileRepository;
import com.api.demo_data_jpa.repository.TextRepository;
import com.api.demo_data_jpa.repository.VideoRepository;

import jakarta.transaction.Transactional;

@Component
public class InheritanceClassExample implements CommandLineRunner {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private TextRepository textRepository;

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

    }

}
