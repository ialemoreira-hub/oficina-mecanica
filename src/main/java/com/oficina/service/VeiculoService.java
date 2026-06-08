package com.oficina.service;

import com.oficina.model.Veiculo;
import com.oficina.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Transactional(readOnly = true)
    public List<Veiculo> listarTodos() {
        List<Veiculo> veiculos = veiculoRepository.findAll();
        veiculos.forEach(v -> {
            if (v.getCliente() != null) {
                v.getCliente().getNome();
            }
        });
        return veiculos;
    }

    @Transactional(readOnly = true)
    public Veiculo buscarPorId(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<Veiculo> listarPorCliente(Long clienteId) {
        return veiculoRepository.findByClienteId(clienteId);
    }

    @Transactional
    public Veiculo salvar(Veiculo veiculo) {
        if (veiculoRepository.existsByPlaca(veiculo.getPlaca()) && veiculo.getId() == null) {
            throw new RuntimeException("Placa já cadastrada.");
        }
        return veiculoRepository.save(veiculo);
    }

    @Transactional
    public void excluir(Long id) {
        veiculoRepository.deleteById(id);
    }
}