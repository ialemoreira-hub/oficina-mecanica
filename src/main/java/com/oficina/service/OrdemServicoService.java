package com.oficina.service;

import com.oficina.model.*;
import com.oficina.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Camada de serviço para Ordens de Serviço.
 * Concentra as regras de negócio do sistema.
 */
@Service
public class OrdemServicoService {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    // ============================================================
    // CRUD
    // ============================================================

    @Transactional
    public OrdemServico abrirOrdem(OrdemServico ordem) {
        ordem.setNumeroOs(gerarNumeroOs());
        ordem.setDataAbertura(LocalDateTime.now());
        ordem.setStatus(StatusOrdem.ABERTA);
        return ordemServicoRepository.save(ordem);
    }

    public List<OrdemServico> listarTodas() {
        return ordemServicoRepository.findAll();
    }

    public OrdemServico buscarPorId(Long id) {
        return ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada: " + id));
    }

    public List<OrdemServico> listarPorStatus(StatusOrdem status) {
        return ordemServicoRepository.findByStatus(status);
    }

    // ============================================================
    // REGRAS DE NEGÓCIO: mudança de status
    // ============================================================

    @Transactional
    public OrdemServico atualizarStatus(Long id, StatusOrdem novoStatus) {
        OrdemServico ordem = buscarPorId(id);
        ordem.setStatus(novoStatus);
        return ordemServicoRepository.save(ordem);
    }

    @Transactional
    public OrdemServico adicionarItem(Long ordemId, ItemOrdem item) {
        OrdemServico ordem = buscarPorId(ordemId);
        item.calcularSubtotal();
        ordem.adicionarItem(item);
        return ordemServicoRepository.save(ordem);
    }

    @Transactional
    public OrdemServico removerItem(Long ordemId, Long itemId) {
        OrdemServico ordem = buscarPorId(ordemId);
        ordem.getItens().removeIf(i -> i.getId().equals(itemId));
        ordem.calcularTotal();
        return ordemServicoRepository.save(ordem);
    }

    // ============================================================
    // PAGAMENTO
    // ============================================================

    @Transactional
    public Pagamento registrarPagamento(Long ordemId, Pagamento.FormaPagamento forma, String obs) {
        OrdemServico ordem = buscarPorId(ordemId);

        if (pagamentoRepository.existsByOrdemServicoId(ordemId)) {
            throw new RuntimeException("Esta OS já possui pagamento registrado.");
        }

        Pagamento pagamento = new Pagamento(ordem, ordem.getValorTotal(), forma);
        pagamento.setObservacoes(obs);
        pagamentoRepository.save(pagamento);

        // Atualiza status para ENTREGUE após pagamento
        ordem.setStatus(StatusOrdem.ENTREGUE);
        ordemServicoRepository.save(ordem);

        return pagamento;
    }

    // ============================================================
    // RELATÓRIOS
    // ============================================================

    public BigDecimal calcularFaturamentoPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        BigDecimal total = ordemServicoRepository.totalFaturadoPeriodo(inicio, fim);
        return total != null ? total : BigDecimal.ZERO;
    }

    public List<Object[]> contarPorStatus() {
        return ordemServicoRepository.countByStatus();
    }

    // ============================================================
    // UTILITÁRIO
    // ============================================================

    private String gerarNumeroOs() {
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = ordemServicoRepository.count() + 1;
        return "OS-" + data + "-" + String.format("%04d", count);
    }
}
