package com.oficina.model;

import jakarta.persistence.*;

/**
 * SUBCLASSE de Usuario — demonstra HERANÇA.
 * Sobrescreve (OVERRIDE) getRole() e getTipoDescricao().
 * Responsável por executar os serviços nas ordens.
 */
@Entity
@DiscriminatorValue("MECANICO")
public class Mecanico extends Usuario {

    @Column(name = "especialidade")
    private String especialidade;

    @Column(name = "crea")
    private String crea;

    // ============================================================
    // CONSTRUTORES
    // ============================================================

    public Mecanico() {
        super();
    }

    public Mecanico(String nome, String email, String senha, String especialidade) {
        super(nome, email, senha);
        this.especialidade = especialidade;
    }

    // ============================================================
    // OVERRIDE - POLIMORFISMO
    // ============================================================

    @Override
    public String getRole() {
        return "ROLE_MECANICO";
    }

    @Override
    public String getTipoDescricao() {
        return "Mecânico";
    }

    // ============================================================
    // GETTERS E SETTERS
    // ============================================================

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public String getCrea() { return crea; }
    public void setCrea(String crea) { this.crea = crea; }
}
