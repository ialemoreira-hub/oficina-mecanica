package com.oficina.repository;

import com.oficina.model.Mecanico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MecanicoRepository extends JpaRepository<Mecanico, Long> {
    List<Mecanico> findByAtivoTrue();
}
