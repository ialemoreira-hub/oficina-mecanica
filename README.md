# Sistema de Gestão de Oficina Mecânica - Backend

Projeto Integrador da disciplina de Programação Orientada a Objetos

**4º Semestre - Análise e Desenvolvimento de Sistemas**

---

## Integrantes

- Iale Moreira - 219494
- Lucas Henrique - 060578
- Luigi Sapucaia - 191145
- José Artur - 217824

---

## Descrição

API REST desenvolvida em Spring Boot para gerenciamento de oficinas mecânicas, responsável pelo controle de usuários, clientes, veículos e ordens de serviço.

---

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.4.5
- Spring Security
- BCrypt
- Spring Data JPA
- Hibernate
- PostgreSQL
- Thymeleaf
- Maven
- Docker

---

## Organização em Camadas

- **model** → classes de domínio
- **repository** → acesso ao banco de dados
- **service** → regras de negócio
- **controller** → endpoints REST e MVC

---

## Conceitos de POO Aplicados

### Herança

Administrador, Atendente e Mecânico estendem a classe Usuario.

### Polimorfismo

Métodos `getTipoUsuario()` e `getTipoDescricao()` possuem comportamentos diferentes em cada subclasse.

### Encapsulamento

Atributos privados utilizando getters e setters.

### Override

Métodos sobrescritos nas subclasses para especialização de comportamento.

---

## Funcionalidades

- Autenticação e autorização de usuários
- Controle de perfis de acesso
- Cadastro de clientes
- Cadastro de veículos
- Cadastro de usuários
- Controle de ordens de serviço
- Controle de peças e serviços
- Dashboard com estatísticas
- Relatórios gerenciais

---

## Como Executar

### Pré-requisitos

- Java 21 ou superior
- PostgreSQL
- Maven
- IntelliJ IDEA

### Configuração do Banco

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/oficina_mecanica
spring.datasource.username=postgres
spring.datasource.password=sua_senha
```

### Execução

```bash
mvn spring-boot:run
```

Backend disponível em:

```text
http://localhost:8080
```

---

## Login Padrão

### Administrador

```text
E-mail: admin@oficina.com
Senha: admin123
```

---

## Deploy

Backend hospedado utilizando Render com PostgreSQL.

---

## Frontend

A aplicação frontend encontra-se na branch **main** deste repositório.

---

## Observação

Durante o desenvolvimento foi utilizado MySQL localmente. Para o deploy em produção no Render, a aplicação foi adaptada para utilizar PostgreSQL.