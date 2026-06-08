package com.oficina.controller;

import com.oficina.model.StatusOrdem;
import com.oficina.repository.OrdemServicoRepository;
import com.oficina.service.OrdemServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired private OrdemServicoService ordemServicoService;
    @Autowired private OrdemServicoRepository ordemServicoRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("statusCount", ordemServicoService.contarPorStatus());
        model.addAttribute("todosStatus", StatusOrdem.values());
        return "relatorios/index";
    }

    @GetMapping("/faturamento")
    public String faturamento(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            Model model) {

        if (inicio == null) inicio = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        if (fim == null) fim = LocalDateTime.now();

        model.addAttribute("inicio", inicio);
        model.addAttribute("fim", fim);
        model.addAttribute("total", ordemServicoService.calcularFaturamentoPeriodo(inicio, fim));
        model.addAttribute("ordens", ordemServicoRepository.findByDataAberturaBetween(inicio, fim));
        return "relatorios/faturamento";
    }
}
