package com.criandoapi.projeto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjetoApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProjetoApplication.class, args);
		System.out.println("🚀 Servidor iniciado em http://localhost:8080");
	}
}
