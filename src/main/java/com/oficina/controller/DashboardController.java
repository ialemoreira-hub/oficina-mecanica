package com.oficina.controller;

import com.oficina.model.StatusOrdem;
import com.oficina.repository.*;
import com.oficina.service.OrdemServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired private ClienteRepository clienteRepository;
    @Autowired private VeiculoRepository veiculoRepository;
    @Autowired private OrdemServicoRepository ordemServicoRepository;
    @Autowired private OrdemServicoService ordemServicoService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalClientes", clienteRepository.count());
        model.addAttribute("totalVeiculos", veiculoRepository.count());
        model.addAttribute("totalOs", ordemServicoRepository.count());
        model.addAttribute("osAbertas", ordemServicoRepository.findByStatus(StatusOrdem.ABERTA).size());
        model.addAttribute("osEmAndamento", ordemServicoRepository.findByStatus(StatusOrdem.EM_ANDAMENTO).size());
        model.addAttribute("osConcluidas", ordemServicoRepository.findByStatus(StatusOrdem.CONCLUIDA).size());
        model.addAttribute("ultimasOs", ordemServicoRepository.findAll()
                .stream().sorted((a, b) -> b.getDataAbertura().compareTo(a.getDataAbertura()))
                .limit(5).toList());
        return "dashboard";
    }

    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "acesso-negado";
    }
}
