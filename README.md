<<<<<<< HEAD
# 🔧 Sistema de Gestão de Oficina Mecânica

Projeto Integrador — Disciplina: Programação Orientada a Objetos  
4º Semestre — Análise e Desenvolvimento de Sistemas

---

## 👥 Integrantes do Grupo

- [Nome 1]
- [Nome 2]
- [Nome 3]
- [Nome 4]

---

## 📌 Tema Sorteado

**Sistema de Gestão de Oficina Mecânica**

---

## 📋 Descrição do Sistema

Sistema web desenvolvido em Java com Spring Boot para gerenciamento completo de uma oficina mecânica. Permite o controle de clientes, veículos, ordens de serviço, peças, serviços, pagamentos e relatórios.

---

## ✅ Conceitos de POO Aplicados

| Conceito | Onde está no código |
|---|---|
| **Herança** | `Administrador`, `Atendente`, `Mecanico` herdam de `Usuario` |
| **Polimorfismo** | Método `getRole()` retorna valores diferentes por tipo de usuário |
| **Override** | `getRole()` e `getTipoDescricao()` sobrescritos em cada subclasse |
| **Encapsulamento** | Todos os atributos são `private` com getters/setters |
| **Construtores** | Todas as classes possuem construtores personalizados |
| **Classes** | 10+ classes próprias do domínio |

---

## 🗂️ Estrutura do Projeto

```
src/main/java/com/oficina/
├── model/          → Classes de domínio (Usuario, Cliente, Veiculo, OrdemServico...)
├── service/        → Regras de negócio (OrdemServicoService, ClienteService...)
├── repository/     → Acesso ao banco de dados (Spring Data JPA)
├── controller/     → Controladores web (rotas HTTP)
└── util/           → Configurações (SecurityConfig, DataInitializer)
```

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2**
- **Spring Security** (autenticação e perfis)
- **Spring Data JPA + Hibernate** (persistência)
- **MySQL** (banco de dados)
- **Thymeleaf** (templates HTML)
- **Bootstrap 5** (interface)
- **Maven** (gerenciador de dependências)

---

## ▶️ Como Executar

### Pré-requisitos
- Java 17+
- MySQL rodando localmente
- Maven

### Passos

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/oficina-mecanica.git
cd oficina-mecanica
```

2. Configure o banco de dados em `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=SUA_SENHA
```

3. Execute o projeto:
```bash
mvn spring-boot:run
```

4. Acesse no navegador:
```
http://localhost:8080
```

### Login padrão
| Campo | Valor |
|---|---|
| E-mail | admin@oficina.com |
| Senha | admin123 |

> O banco de dados é criado automaticamente na primeira execução.

---

## 📱 Funcionalidades

- [x] Login com perfis (Administrador, Atendente, Mecânico)
- [x] Dashboard com resumo do sistema
- [x] Cadastro de clientes (CRUD completo)
- [x] Cadastro de veículos vinculados a clientes
- [x] Abertura e gerenciamento de Ordens de Serviço
- [x] Adição de peças e serviços na OS
- [x] Cálculo automático do valor total
- [x] Controle de status da OS (Aberta → Em Andamento → Concluída → Entregue)
- [x] Registro de pagamento
- [x] Relatório de OS por status
- [x] Relatório de faturamento por período

---

## 🏗️ Hierarquia de Classes

```
Usuario (abstract)
├── Administrador  → getRole() = "ROLE_ADMIN"
├── Atendente      → getRole() = "ROLE_ATENDENTE"
└── Mecanico       → getRole() = "ROLE_MECANICO"
```
=======
# oficina-mecanica
>>>>>>> 46837df5b537478af0f0c325b830cdd9f1c17538
