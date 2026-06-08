package com.oficina.controller;

import com.oficina.model.Cliente;
import com.oficina.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listar(Model model, @RequestParam(required = false) String busca) {
        if (busca != null && !busca.isBlank()) {
            model.addAttribute("clientes", clienteService.buscarPorNome(busca));
            model.addAttribute("busca", busca);
        } else {
            model.addAttribute("clientes", clienteService.listarTodos());
        }
        return "clientes/lista";
    }

    @GetMapping("/novo")
    public String formularioNovo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/formulario";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", clienteService.buscarPorId(id));
        return "clientes/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Cliente cliente, BindingResult result,
                         RedirectAttributes attrs) {
        if (result.hasErrors()) return "clientes/formulario";
        try {
            clienteService.salvar(cliente);
            attrs.addFlashAttribute("sucesso", "Cliente salvo com sucesso!");
        } catch (Exception e) {
            attrs.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            clienteService.excluir(id);
            attrs.addFlashAttribute("sucesso", "Cliente excluído com sucesso!");
        } catch (Exception e) {
            attrs.addFlashAttribute("erro", "Não foi possível excluir: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", clienteService.buscarPorId(id));
        return "clientes/detalhe";
    }

    private Map<String, Object> toMap(Cliente c) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", c.getId());
        map.put("nome", c.getNome());
        map.put("cpfCnpj", c.getCpfCnpj());
        map.put("telefone", c.getTelefone());
        map.put("email", c.getEmail());
        map.put("endereco", c.getEndereco());
        return map;
    }

    @GetMapping(value = "/api", produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> listarApi() {
        return clienteService.listarTodos().stream()
                .map(this::toMap)
                .toList();
    }

    @PostMapping(value = "/api", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Map<String, Object> salvarApi(@RequestBody Cliente cliente) {
        return toMap(clienteService.salvar(cliente));
    }

    @PutMapping(value = "/api/{id}", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Map<String, Object> editarApi(@PathVariable Long id, @RequestBody Cliente cliente) {
        cliente.setId(id);
        return toMap(clienteService.salvar(cliente));
    }

    @DeleteMapping(value = "/api/{id}", produces = "application/json")
    @ResponseBody
    public void excluirApi(@PathVariable Long id) {
        clienteService.excluir(id);
    }
}