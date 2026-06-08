package com.oficina.repository;

import com.oficina.model.OrdemServico;
import com.oficina.model.StatusOrdem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    Optional<OrdemServico> findByNumeroOs(String numeroOs);

    List<OrdemServico> findByStatus(StatusOrdem status);

    List<OrdemServico> findByVeiculoClienteId(Long clienteId);

    List<OrdemServico> findByMecanicoId(Long mecanicoId);

    List<OrdemServico> findByDataAberturaBetween(LocalDateTime inicio, LocalDateTime fim);

    // Relatório: conta ordens por status
    @Query("SELECT o.status, COUNT(o) FROM OrdemServico o GROUP BY o.status")
    List<Object[]> countByStatus();

    // Relatório: faturamento total por período
    @Query("SELECT SUM(o.valorTotal) FROM OrdemServico o WHERE o.status IN ('CONCLUIDA','ENTREGUE') AND o.dataAbertura BETWEEN :inicio AND :fim")
    java.math.BigDecimal totalFaturadoPeriodo(LocalDateTime inicio, LocalDateTime fim);
}
