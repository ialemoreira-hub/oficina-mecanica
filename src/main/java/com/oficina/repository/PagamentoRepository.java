package com.oficina.repository;

import com.oficina.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    Optional<Pagamento> findByOrdemServicoId(Long ordemServicoId);
    boolean existsByOrdemServicoId(Long ordemServicoId);
}
