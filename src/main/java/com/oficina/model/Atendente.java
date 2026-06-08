package com.oficina.model;

import jakarta.persistence.*;

/**
 * SUBCLASSE de Usuario — demonstra HERANÇA.
 * Sobrescreve (OVERRIDE) getRole() e getTipoDescricao().
 * Responsável por abrir ordens de serviço e atender clientes.
 */
@Entity
@DiscriminatorValue("ATENDENTE")
public class Atendente extends Usuario {

    @Column(name = "matricula", unique = true)
    private String matricula;

    // ============================================================
    // CONSTRUTORES
    // ============================================================

    public Atendente() {
        super();
    }

    public Atendente(String nome, String email, String senha, String matricula) {
        super(nome, email, senha);
        this.matricula = matricula;
    }

    // ============================================================
    // OVERRIDE - POLIMORFISMO
    // ============================================================

    @Override
    public String getRole() {
        return "ROLE_ATENDENTE";
    }

    @Override
    public String getTipoDescricao() {
        return "Atendente";
    }

    // ============================================================
    // GETTERS E SETTERS
    // ============================================================

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
}
