package com.oficina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OficinaApplication {

    public static void main(String[] args) {
        SpringApplication.run(OficinaApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("  Sistema de Oficina Mecânica");
        System.out.println("  Acesse: http://localhost:8080");
        System.out.println("========================================\n");
    }
}