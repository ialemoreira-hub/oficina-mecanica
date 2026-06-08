package com.oficina.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Registra o pagamento de uma Ordem de Serviço.
 */
@Entity
@Table(name = "pagamentos")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_servico_id", nullable = false, unique = true)
    private OrdemServico ordemServico;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false)
    private FormaPagamento formaPagamento;

    @Column(name = "data_pagamento", nullable = false)
    private LocalDateTime dataPagamento;

    @Column
    private String observacoes;

    // ============================================================
    // CONSTRUTORES
    // ============================================================

    public Pagamento() {
        this.dataPagamento = LocalDateTime.now();
    }

    public Pagamento(OrdemServico ordemServico, BigDecimal valor, FormaPagamento formaPagamento) {
        this();
        this.ordemServico = ordemServico;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
    }

    // ============================================================
    // GETTERS E SETTERS
    // ============================================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public OrdemServico getOrdemServico() { return ordemServico; }
    public void setOrdemServico(OrdemServico ordemServico) { this.ordemServico = ordemServico; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    // ============================================================
    // ENUM
    // ============================================================

    public enum FormaPagamento {
        DINHEIRO("Dinheiro"),
        CARTAO_CREDITO("Cartão de Crédito"),
        CARTAO_DEBITO("Cartão de Débito"),
        PIX("Pix"),
        TRANSFERENCIA("Transferência"),
        BOLETO("Boleto");

        private final String descricao;
        FormaPagamento(String descricao) { this.descricao = descricao; }
        public String getDescricao() { return descricao; }
    }
}
