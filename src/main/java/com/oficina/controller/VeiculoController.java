package com.oficina.controller;

import com.oficina.model.Veiculo;
import com.oficina.service.ClienteService;
import com.oficina.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired private VeiculoService veiculoService;
    @Autowired private ClienteService clienteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("veiculos", veiculoService.listarTodos());
        return "veiculos/lista";
    }

    @GetMapping("/novo")
    public String formularioNovo(Model model) {
        model.addAttribute("veiculo", new Veiculo());
        model.addAttribute("clientes", clienteService.listarTodos());
        return "veiculos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("veiculo", veiculoService.buscarPorId(id));
        model.addAttribute("clientes", clienteService.listarTodos());
        return "veiculos/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Veiculo veiculo,
                         @RequestParam Long clienteId,
                         RedirectAttributes attrs) {
        try {
            veiculo.setCliente(clienteService.buscarPorId(clienteId));
            veiculoService.salvar(veiculo);
            attrs.addFlashAttribute("sucesso", "Veículo salvo com sucesso!");
        } catch (Exception e) {
            attrs.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/veiculos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            veiculoService.excluir(id);
            attrs.addFlashAttribute("sucesso", "Veículo excluído.");
        } catch (Exception e) {
            attrs.addFlashAttribute("erro", "Não foi possível excluir: " + e.getMessage());
        }
        return "redirect:/veiculos";
    }

    @GetMapping(value = "/api", produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> listarApi() {
        return veiculoService.listarTodos().stream()
                .map(v -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", v.getId());
                    map.put("placa", v.getPlaca());
                    map.put("marca", v.getMarca());
                    map.put("modelo", v.getModelo());
                    map.put("ano", v.getAno());
                    map.put("cor", v.getCor());
                    map.put("clienteId", v.getCliente().getId());
                    map.put("clienteNome", v.getCliente().getNome());
                    return map;
                })
                .toList();
    }

    @PostMapping(value = "/api", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Map<String, Object> salvarApi(@RequestBody Map<String, Object> dados) {
        Veiculo veiculo = new Veiculo();
        if (dados.get("id") != null) {
            veiculo.setId(Long.valueOf(dados.get("id").toString()));
        }
        veiculo.setPlaca(dados.get("placa").toString().toUpperCase());
        veiculo.setMarca(dados.get("marca").toString());
        veiculo.setModelo(dados.get("modelo").toString());
        veiculo.setAno(Integer.valueOf(dados.get("ano").toString()));
        if (dados.get("cor") != null) veiculo.setCor(dados.get("cor").toString());
        Long clienteId = Long.valueOf(dados.get("clienteId").toString());
        veiculo.setCliente(clienteService.buscarPorId(clienteId));
        Veiculo salvo = veiculoService.salvar(veiculo);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", salvo.getId());
        map.put("placa", salvo.getPlaca());
        map.put("marca", salvo.getMarca());
        map.put("modelo", salvo.getModelo());
        map.put("ano", salvo.getAno());
        map.put("cor", salvo.getCor());
        map.put("clienteId", salvo.getCliente().getId());
        map.put("clienteNome", salvo.getCliente().getNome());
        return map;
    }

    @DeleteMapping(value = "/api/{id}", produces = "application/json")
    @ResponseBody
    public void excluirApi(@PathVariable Long id) {
        veiculoService.excluir(id);
    }
}