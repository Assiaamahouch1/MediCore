package com.example.cabinetservice;

import com.example.cabinetservice.repository.CabinetRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class CabinetServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CabinetServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner testData(CabinetRepository repo) {
        return args -> {
            System.out.println("Cabinets en base : " + repo.findAll());
        };
    }

}
