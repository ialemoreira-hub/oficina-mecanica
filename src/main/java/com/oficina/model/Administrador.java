package com.oficina.model;

import jakarta.persistence.*;

/**
 * SUBCLASSE de Usuario — demonstra HERANÇA.
 * Sobrescreve (OVERRIDE) os métodos getRole() e getTipoDescricao().
 * Tem acesso total ao sistema.
 */
@Entity
@DiscriminatorValue("ADMINISTRADOR")
public class Administrador extends Usuario {

    // Administrador pode ter campos extras específicos
    @Column(name = "nivel_acesso")
    private String nivelAcesso = "TOTAL";

    // ============================================================
    // CONSTRUTORES
    // ============================================================

    public Administrador() {
        super();
    }

    public Administrador(String nome, String email, String senha) {
        super(nome, email, senha);
        this.nivelAcesso = "TOTAL";
    }

    // ============================================================
    // OVERRIDE - POLIMORFISMO em ação
    // ============================================================

    @Override
    public String getRole() {
        return "ROLE_ADMIN"; // permissão de administrador no Spring Security
    }

    @Override
    public String getTipoDescricao() {
        return "Administrador";
    }

    // ============================================================
    // GETTERS E SETTERS
    // ============================================================

    public String getNivelAcesso() { return nivelAcesso; }
    public void setNivelAcesso(String nivelAcesso) { this.nivelAcesso = nivelAcesso; }
}
