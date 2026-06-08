package com.oficina.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Representa um item (peça ou serviço) dentro de uma Ordem de Serviço.
 * O subtotal é calculado automaticamente (quantidade × valor unitário).
 */
@Entity
@Table(name = "itens_ordem")
public class ItemOrdem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico ordemServico;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoItem tipo; // PECA ou SERVICO

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "valor_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;

    // ============================================================
    // CONSTRUTORES
    // ============================================================

    public ItemOrdem() {}

    public ItemOrdem(String descricao, TipoItem tipo, Integer quantidade, BigDecimal valorUnitario) {
        this.descricao = descricao;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        calcularSubtotal();
    }

    // ============================================================
    // REGRA DE NEGÓCIO
    // ============================================================

    public void calcularSubtotal() {
        if (quantidade != null && valorUnitario != null) {
            this.subtotal = valorUnitario.multiply(BigDecimal.valueOf(quantidade));
        }
    }

    // ============================================================
    // GETTERS E SETTERS
    // ============================================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public OrdemServico getOrdemServico() { return ordemServico; }
    public void setOrdemServico(OrdemServico ordemServico) { this.ordemServico = ordemServico; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public TipoItem getTipo() { return tipo; }
    public void setTipo(TipoItem tipo) { this.tipo = tipo; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        calcularSubtotal();
    }

    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
        calcularSubtotal();
    }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    // ============================================================
    // ENUM interno
    // ============================================================

    public enum TipoItem {
        PECA("Peça"),
        SERVICO("Serviço");

        private final String descricao;
        TipoItem(String descricao) { this.descricao = descricao; }
        public String getDescricao() { return descricao; }
    }
}
