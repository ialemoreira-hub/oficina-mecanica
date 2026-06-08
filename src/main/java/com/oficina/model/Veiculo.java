package com.oficina.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Representa o veículo do cliente.
 * Cada veículo está vinculado a um cliente (ManyToOne).
 */
@Entity
@Table(name = "veiculos")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Placa é obrigatória")
    @Column(nullable = false, unique = true)
    private String placa;

    @NotBlank(message = "Marca é obrigatória")
    @Column(nullable = false)
    private String marca;

    @NotBlank(message = "Modelo é obrigatório")
    @Column(nullable = false)
    private String modelo;

    @NotNull(message = "Ano é obrigatório")
    @Column(nullable = false)
    private Integer ano;

    @Column
    private String cor;

    @Column
    private String quilometragem;

    // Vínculo com o cliente (REGRA: cada veículo pertence a um cliente)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // ============================================================
    // CONSTRUTORES
    // ============================================================

    public Veiculo() {}

    public Veiculo(String placa, String marca, String modelo, Integer ano, Cliente cliente) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.cliente = cliente;
    }

    // ============================================================
    // GETTERS E SETTERS
    // ============================================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa.toUpperCase(); }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public String getQuilometragem() { return quilometragem; }
    public void setQuilometragem(String quilometragem) { this.quilometragem = quilometragem; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public String getDescricaoCompleta() {
        return ano + " " + marca + " " + modelo + " - " + placa;
    }

    @Override
    public String toString() {
        return "Veiculo{placa='" + placa + "', modelo='" + marca + " " + modelo + "', ano=" + ano + "}";
    }
}
