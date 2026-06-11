# Sistema de Gestao de Oficina Mecanica

Projeto Integrador da disciplina de Programacao Orientada a Objetos  
4 Semestre - Analise e Desenvolvimento de Sistemas

---

## Integrantes

- Iale Moreira - 219494
- Lucas Henrique - 060578
- Luigi Sapucaia - 191145
- Jose Artur - 217824

---

## Tema Sorteado

Sistema de Gestao de Oficina Mecanica

---

## Descricao do Sistema

Sistema web completo para gerenciamento de uma oficina mecanica, permitindo controle de clientes, veiculos, ordens de servico, usuarios com perfis diferentes e relatorios gerenciais.

---

## Tecnologias Utilizadas

### Backend
- Java 21 + Spring Boot 3.4.5
- Spring Security + BCrypt
- Spring Data JPA + Hibernate
- MySQL 8.0
- Thymeleaf

### Frontend
- Angular 21
- TypeScript
- HTML5 + CSS3

---

## Organizacao em Camadas

- model: classes de dominio da aplicacao
- repository: acesso ao banco de dados
- service: regras de negocio
- controller: endpoints REST e MVC

---

## Conceitos de POO Aplicados

- Heranca: Administrador, Atendente e Mecanico estendem Usuario
- Polimorfismo: metodos getTipoUsuario() e getTipoDescricao() com comportamento diferente em cada subclasse
- Encapsulamento: atributos privados com getters e setters
- Override: getTipoUsuario() e getTipoDescricao() sobrescritos em cada subclasse

---

## Como Executar

### Pre-requisitos
- Java 21 ou superior
- MySQL 8.0
- Node.js 20 ou superior com Angular CLI
- IntelliJ IDEA

### Backend (branch: backend)
1. Configure o MySQL com banco oficina_mecanica, usuario root, senha root123
2. Abra o projeto no IntelliJ IDEA
3. Execute OficinaApplication.java
4. Backend disponivel em http://localhost:8080

### Frontend (branch: main)
1. Instale as dependencias com: npm install
2. Inicie o servidor com: ng serve --port 4200
3. Frontend disponivel em http://localhost:4200

---

## Login Padrao

- E-mail: admin@oficina.com
- Senha: admin123

---

## Funcionalidades

- Login com perfis de acesso: Administrador, Atendente e Mecanico
- Cadastro, edicao, exclusao e listagem de clientes
- Cadastro, edicao, exclusao e listagem de veiculos
- Abertura e gerenciamento de ordens de servico
- Adicao de pecas e servicos nas ordens
- Controle de status das ordens
- Dashboard com estatisticas gerais
- Relatorios gerenciais
- Gestao de usuarios com restricao por perfil
