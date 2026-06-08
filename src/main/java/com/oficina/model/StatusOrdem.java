package com.oficina.model;

/**
 * Estados possíveis de uma Ordem de Serviço.
 * Representa o ciclo de vida do atendimento na oficina.
 */
public enum StatusOrdem {

    ABERTA("Aberta"),
    EM_ANDAMENTO("Em Andamento"),
    AGUARDANDO_PECA("Aguardando Peça"),
    CONCLUIDA("Concluída"),
    ENTREGUE("Entregue"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusOrdem(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
