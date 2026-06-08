package com.oficina.controller;

import com.oficina.model.*;
import com.oficina.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RequestMapping("/ordens")
public class OrdemServicoController {

    @Autowired private OrdemServicoService ordemServicoService;
    @Autowired private ClienteService clienteService;
    @Autowired private VeiculoService veiculoService;
    @Autowired private UsuarioService usuarioService;

    @GetMapping
    public String listar(Model model, @RequestParam(required = false) String status) {
        if (status != null && !status.isBlank()) {
            StatusOrdem s = StatusOrdem.valueOf(status);
            model.addAttribute("ordens", ordemServicoService.listarPorStatus(s));
            model.addAttribute("statusFiltro", s);
        } else {
            model.addAttribute("ordens", ordemServicoService.listarTodas());
        }
        model.addAttribute("todosStatus", StatusOrdem.values());
        return "ordens/lista";
    }

    @GetMapping("/nova")
    public String formularioNova(Model model) {
        model.addAttribute("ordem", new OrdemServico());
        model.addAttribute("veiculos", veiculoService.listarTodos());
        model.addAttribute("mecanicos", usuarioService.listarMecanicos());
        return "ordens/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute OrdemServico ordem,
                         @RequestParam Long veiculoId,
                         @RequestParam(required = false) Long mecanicoId,
                         RedirectAttributes attrs) {
        try {
            ordem.setVeiculo(veiculoService.buscarPorId(veiculoId));
            if (mecanicoId != null) {
                ordem.setMecanico((Mecanico) usuarioService.buscarPorId(mecanicoId));
            }
            OrdemServico salva = ordemServicoService.abrirOrdem(ordem);
            attrs.addFlashAttribute("sucesso", "OS " + salva.getNumeroOs() + " aberta com sucesso!");
            return "redirect:/ordens/" + salva.getId();
        } catch (Exception e) {
            attrs.addFlashAttribute("erro", e.getMessage());
            return "redirect:/ordens/nova";
        }
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        model.addAttribute("ordem", ordemServicoService.buscarPorId(id));
        model.addAttribute("todosStatus", StatusOrdem.values());
        model.addAttribute("tiposItem", ItemOrdem.TipoItem.values());
        return "ordens/detalhe";
    }

    @PostMapping("/{id}/status")
    public String atualizarStatus(@PathVariable Long id,
                                  @RequestParam StatusOrdem status,
                                  RedirectAttributes attrs) {
        ordemServicoService.atualizarStatus(id, status);
        attrs.addFlashAttribute("sucesso", "Status atualizado para: " + status.getDescricao());
        return "redirect:/ordens/" + id;
    }

    @PostMapping("/{id}/itens")
    public String adicionarItem(@PathVariable Long id,
                                @RequestParam String descricao,
                                @RequestParam ItemOrdem.TipoItem tipo,
                                @RequestParam Integer quantidade,
                                @RequestParam BigDecimal valorUnitario,
                                RedirectAttributes attrs) {
        try {
            ItemOrdem item = new ItemOrdem(descricao, tipo, quantidade, valorUnitario);
            ordemServicoService.adicionarItem(id, item);
            attrs.addFlashAttribute("sucesso", "Item adicionado!");
        } catch (Exception e) {
            attrs.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/ordens/" + id;
    }

    @GetMapping("/{id}/itens/{itemId}/remover")
    public String removerItem(@PathVariable Long id, @PathVariable Long itemId,
                              RedirectAttributes attrs) {
        ordemServicoService.removerItem(id, itemId);
        attrs.addFlashAttribute("sucesso", "Item removido.");
        return "redirect:/ordens/" + id;
    }

    @PostMapping("/{id}/pagamento")
    public String registrarPagamento(@PathVariable Long id,
                                     @RequestParam Pagamento.FormaPagamento formaPagamento,
                                     @RequestParam(required = false) String observacoes,
                                     RedirectAttributes attrs) {
        try {
            ordemServicoService.registrarPagamento(id, formaPagamento, observacoes);
            attrs.addFlashAttribute("sucesso", "Pagamento registrado com sucesso!");
        } catch (Exception e) {
            attrs.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/ordens/" + id;
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/api", produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> listarApi() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return ordemServicoService.listarTodas().stream()
                .map(os -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", os.getId());
                    map.put("numeroOs", os.getNumeroOs());
                    map.put("status", os.getStatus().name());
                    map.put("statusLabel", os.getStatus().getDescricao());
                    map.put("veiculo", os.getVeiculo().getDescricaoCompleta());
                    map.put("cliente", os.getVeiculo().getCliente().getNome());
                    map.put("descricaoProblema", os.getDescricaoProblema());
                    map.put("valorTotal", os.getValorTotal());
                    map.put("dataAbertura", os.getDataAbertura() != null ? os.getDataAbertura().format(fmt) : null);
                    return map;
                })
                .toList();
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/api/{id}", produces = "application/json")
    @ResponseBody
    public Map<String, Object> buscarApi(@PathVariable Long id) {
        OrdemServico os = ordemServicoService.buscarPorId(id);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", os.getId());
        map.put("numeroOs", os.getNumeroOs());
        map.put("status", os.getStatus().name());
        map.put("statusLabel", os.getStatus().getDescricao());
        map.put("veiculo", os.getVeiculo().getDescricaoCompleta());
        map.put("cliente", os.getVeiculo().getCliente().getNome());
        map.put("descricaoProblema", os.getDescricaoProblema());
        map.put("valorTotal", os.getValorTotal());
        List<Map<String, Object>> itens = os.getItens().stream()
                .map(item -> {
                    Map<String, Object> i = new LinkedHashMap<>();
                    i.put("id", item.getId());
                    i.put("descricao", item.getDescricao());
                    i.put("tipo", item.getTipo().getDescricao());
                    i.put("quantidade", item.getQuantidade());
                    i.put("valorUnitario", item.getValorUnitario());
                    i.put("subtotal", item.getSubtotal());
                    return i;
                }).toList();
        map.put("itens", itens);
        return map;
    }

    @Transactional
    @PostMapping(value = "/api", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Map<String, Object> abrirApi(@RequestBody Map<String, Object> dados) {
        OrdemServico ordem = new OrdemServico();
        Long veiculoId = Long.valueOf(dados.get("veiculoId").toString());
        ordem.setVeiculo(veiculoService.buscarPorId(veiculoId));
        ordem.setDescricaoProblema(dados.get("descricaoProblema").toString());
        OrdemServico salva = ordemServicoService.abrirOrdem(ordem);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", salva.getId());
        map.put("numeroOs", salva.getNumeroOs());
        map.put("status", salva.getStatus().name());
        return map;
    }

    @Transactional
    @PostMapping(value = "/api/{id}/status", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Map<String, Object> atualizarStatusApi(@PathVariable Long id,
                                                  @RequestBody Map<String, Object> dados) {
        StatusOrdem status = StatusOrdem.valueOf(dados.get("status").toString());
        OrdemServico os = ordemServicoService.atualizarStatus(id, status);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", os.getId());
        map.put("numeroOs", os.getNumeroOs());
        map.put("status", os.getStatus().name());
        map.put("statusLabel", os.getStatus().getDescricao());
        map.put("valorTotal", os.getValorTotal());
        map.put("veiculo", os.getVeiculo().getDescricaoCompleta());
        map.put("cliente", os.getVeiculo().getCliente().getNome());
        map.put("descricaoProblema", os.getDescricaoProblema());
        List<Map<String, Object>> itens = os.getItens().stream()
                .map(item -> {
                    Map<String, Object> i = new LinkedHashMap<>();
                    i.put("descricao", item.getDescricao());
                    i.put("tipo", item.getTipo().getDescricao());
                    i.put("quantidade", item.getQuantidade());
                    i.put("valorUnitario", item.getValorUnitario());
                    i.put("subtotal", item.getSubtotal());
                    return i;
                }).toList();
        map.put("itens", itens);
        return map;
    }

    @Transactional
    @PostMapping(value = "/api/{id}/itens", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Map<String, Object> adicionarItemApi(@PathVariable Long id,
                                                @RequestBody Map<String, Object> dados) {
        String descricao = dados.get("descricao").toString();
        ItemOrdem.TipoItem tipo = ItemOrdem.TipoItem.valueOf(dados.get("tipo").toString());
        Integer quantidade = Integer.valueOf(dados.get("quantidade").toString());
        BigDecimal valorUnitario = new BigDecimal(dados.get("valorUnitario").toString());
        ItemOrdem item = new ItemOrdem(descricao, tipo, quantidade, valorUnitario);
        OrdemServico os = ordemServicoService.adicionarItem(id, item);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", os.getId());
        map.put("numeroOs", os.getNumeroOs());
        map.put("status", os.getStatus().name());
        map.put("statusLabel", os.getStatus().getDescricao());
        map.put("valorTotal", os.getValorTotal());
        map.put("veiculo", os.getVeiculo().getDescricaoCompleta());
        map.put("cliente", os.getVeiculo().getCliente().getNome());
        map.put("descricaoProblema", os.getDescricaoProblema());
        List<Map<String, Object>> itens = os.getItens().stream()
                .map(i -> {
                    Map<String, Object> im = new LinkedHashMap<>();
                    im.put("descricao", i.getDescricao());
                    im.put("tipo", i.getTipo().getDescricao());
                    im.put("quantidade", i.getQuantidade());
                    im.put("valorUnitario", i.getValorUnitario());
                    im.put("subtotal", i.getSubtotal());
                    return im;
                }).toList();
        map.put("itens", itens);
        return map;
    }
}