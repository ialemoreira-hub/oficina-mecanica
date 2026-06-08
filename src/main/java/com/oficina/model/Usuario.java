package com.oficina.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

/**
 * CLASSE PAI - base da hierarquia de herança.
 * Administrador, Atendente e Mecanico herdam desta classe.
 *
 * POO aplicado:
 *  - Encapsulamento: atributos private com getters/setters
 *  - Herança: subclasses estendem esta classe
 *  - Polimorfismo: método getRole() é sobrescrito nas subclasses (override)
 */
@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING)
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @Email(message = "E-mail inválido")
    @NotBlank(message = "E-mail é obrigatório")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Column(nullable = false)
    private String senha;

    @Column(name = "tipo_usuario", insertable = false, updatable = false)
    private String tipoUsuario;

    @Column(nullable = false)
    private boolean ativo = true;

    // ============================================================
    // CONSTRUTORES
    // ============================================================

    public Usuario() {}

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    // ============================================================
    // MÉTODO ABSTRATO - obriga cada subclasse a implementar
    // Isso é POLIMORFISMO + OVERRIDE na prática
    // ============================================================

    /**
     * Cada perfil retorna seu próprio papel/permissão.
     * Administrador retorna "ROLE_ADMIN"
     * Atendente retorna "ROLE_ATENDENTE"
     * Mecanico retorna "ROLE_MECANICO"
     */
    public abstract String getRole();

    /**
     * Descrição legível do tipo de usuário.
     * Cada subclasse sobrescreve com seu próprio texto.
     */
    public abstract String getTipoDescricao();

    // ============================================================
    // GETTERS E SETTERS (Encapsulamento)
    // ============================================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getTipoUsuario() { return tipoUsuario; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nome='" + nome + "', email='" + email + "', tipo=" + getTipoDescricao() + "}";
    }
}
