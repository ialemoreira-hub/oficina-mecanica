package com.oficina.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Ordem de Serviço — coração do sistema.
 * Vincula veículo, mecânico, peças e serviços.
 * O valor total é calculado automaticamente.
 */
@Entity
@Table(name = "ordens_servico")
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_os", unique = true, nullable = false)
    private String numeroOs;

    // Vínculo com veículo (REGRA: OS deve estar vinculada a um veículo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    // Mecânico responsável
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mecanico_id")
    private Mecanico mecanico;

    // Atendente que abriu a OS
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atendente_id")
    private Usuario atendente;

    @Column(columnDefinition = "TEXT")
    private String descricaoProblema;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrdem status = StatusOrdem.ABERTA;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;

    // Itens da OS: peças + serviços
    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOrdem> itens = new ArrayList<>();

    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    // ============================================================
    // CONSTRUTORES
    // ============================================================

    public OrdemServico() {
        this.dataAbertura = LocalDateTime.now();
        this.status = StatusOrdem.ABERTA;
    }

    public OrdemServico(String numeroOs, Veiculo veiculo, String descricaoProblema) {
        this();
        this.numeroOs = numeroOs;
        this.veiculo = veiculo;
        this.descricaoProblema = descricaoProblema;
    }

    // ============================================================
    // REGRA DE NEGÓCIO: calcula valor total automaticamente
    // ============================================================

    public void calcularTotal() {
        this.valorTotal = itens.stream()
                .map(ItemOrdem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void adicionarItem(ItemOrdem item) {
        item.setOrdemServico(this);
        this.itens.add(item);
        calcularTotal();
    }

    public void removerItem(ItemOrdem item) {
        this.itens.remove(item);
        calcularTotal();
    }

    // ============================================================
    // GETTERS E SETTERS
    // ============================================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroOs() { return numeroOs; }
    public void setNumeroOs(String numeroOs) { this.numeroOs = numeroOs; }

    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }

    public Mecanico getMecanico() { return mecanico; }
    public void setMecanico(Mecanico mecanico) { this.mecanico = mecanico; }

    public Usuario getAtendente() { return atendente; }
    public void setAtendente(Usuario atendente) { this.atendente = atendente; }

    public String getDescricaoProblema() { return descricaoProblema; }
    public void setDescricaoProblema(String descricaoProblema) { this.descricaoProblema = descricaoProblema; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public StatusOrdem getStatus() { return status; }
    public void setStatus(StatusOrdem status) {
        this.status = status;
        if (status == StatusOrdem.CONCLUIDA && this.dataConclusao == null) {
            this.dataConclusao = LocalDateTime.now();
        }
        if (status == StatusOrdem.ENTREGUE && this.dataEntrega == null) {
            this.dataEntrega = LocalDateTime.now();
        }
    }

    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }

    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }

    public LocalDateTime getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(LocalDateTime dataEntrega) { this.dataEntrega = dataEntrega; }

    public List<ItemOrdem> getItens() { return itens; }
    public void setItens(List<ItemOrdem> itens) { this.itens = itens; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
}
