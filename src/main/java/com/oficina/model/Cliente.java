package com.oficina.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa o cliente da oficina.
 * Um cliente pode ter vários veículos.
 */
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "CPF/CNPJ é obrigatório")
    @Column(nullable = false, unique = true)
    private String cpfCnpj;

    @Column
    private String telefone;

    @Email(message = "E-mail inválido")
    @Column
    private String email;

    @Column
    private String endereco;

    // Um cliente pode ter vários veículos
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Veiculo> veiculos = new ArrayList<>();

    // ============================================================
    // CONSTRUTORES
    // ============================================================

    public Cliente() {}

    public Cliente(String nome, String cpfCnpj, String telefone, String email) {
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.telefone = telefone;
        this.email = email;
    }

    // ============================================================
    // GETTERS E SETTERS
    // ============================================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpfCnpj() { return cpfCnpj; }
    public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public List<Veiculo> getVeiculos() { return veiculos; }
    public void setVeiculos(List<Veiculo> veiculos) { this.veiculos = veiculos; }

    @Override
    public String toString() {
        return "Cliente{id=" + id + ", nome='" + nome + "', cpfCnpj='" + cpfCnpj + "'}";
    }
}
