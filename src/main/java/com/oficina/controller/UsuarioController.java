package com.oficina.controller;

import com.oficina.model.*;
import com.oficina.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }

    @GetMapping("/novo")
    public String formularioNovo(Model model) {
        model.addAttribute("tiposSelecionaveis", new String[]{"ADMINISTRADOR", "ATENDENTE", "MECANICO"});
        return "usuarios/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam String nome,
                         @RequestParam String email,
                         @RequestParam String senha,
                         @RequestParam String tipo,
                         @RequestParam(required = false) String matricula,
                         @RequestParam(required = false) String especialidade,
                         RedirectAttributes attrs) {
        try {
            Usuario usuario;
            switch (tipo) {
                case "ADMINISTRADOR" -> usuario = new Administrador(nome, email, senha);
                case "ATENDENTE"     -> usuario = new Atendente(nome, email, senha, matricula);
                case "MECANICO"      -> usuario = new Mecanico(nome, email, senha, especialidade);
                default -> throw new RuntimeException("Tipo inválido.");
            }
            usuarioService.salvar(usuario);
            attrs.addFlashAttribute("sucesso", "Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            attrs.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/desativar/{id}")
    public String desativar(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            usuarioService.excluir(id);
            attrs.addFlashAttribute("sucesso", "Usuário desativado.");
        } catch (Exception e) {
            attrs.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @GetMapping(value = "/api", produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> listarApi() {
        return usuarioService.listarTodos().stream()
                .map(u -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", u.getId());
                    map.put("nome", u.getNome());
                    map.put("email", u.getEmail());
                    map.put("tipoUsuario", u.getTipoUsuario());
                    map.put("tipoDescricao", u.getTipoDescricao());
                    map.put("ativo", u.isAtivo());
                    return map;
                })
                .toList();
    }

    @PostMapping(value = "/api/login", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> loginApi(@RequestBody Map<String, Object> dados) {
        String email = dados.get("email").toString();
        String senha = dados.get("senha").toString();

        return usuarioService.listarTodos().stream()
                .filter(u -> u.getEmail().equals(email) && u.isAtivo())
                .filter(u -> passwordEncoder.matches(senha, u.getSenha()))
                .findFirst()
                .map(u -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", u.getId());
                    map.put("nome", u.getNome());
                    map.put("email", u.getEmail());
                    map.put("perfil", u.getTipoUsuario());
                    return ResponseEntity.ok(map);
                })
                .orElse(ResponseEntity.status(401).build());
    }

    @PostMapping(value = "/api", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Map<String, Object> salvarApi(@RequestBody Map<String, Object> dados) {
        String tipo = dados.get("tipo").toString();
        String nome = dados.get("nome").toString();
        String email = dados.get("email").toString();
        String senha = dados.get("senha").toString();

        Usuario usuario;
        switch (tipo) {
            case "ADMINISTRADOR" -> usuario = new Administrador(nome, email, senha);
            case "ATENDENTE" -> {
                String matricula = dados.get("matricula") != null ? dados.get("matricula").toString() : null;
                usuario = new Atendente(nome, email, senha, matricula);
            }
            case "MECANICO" -> {
                String especialidade = dados.get("especialidade") != null ? dados.get("especialidade").toString() : null;
                usuario = new Mecanico(nome, email, senha, especialidade);
            }
            default -> throw new RuntimeException("Tipo inválido.");
        }

        Usuario salvo = usuarioService.salvar(usuario);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", salvo.getId());
        map.put("nome", salvo.getNome());
        map.put("email", salvo.getEmail());
        map.put("tipoDescricao", salvo.getTipoDescricao());
        return map;
    }

    @DeleteMapping(value = "/api/{id}", produces = "application/json")
    @ResponseBody
    public void desativarApi(@PathVariable Long id) {
        usuarioService.excluir(id);
    }
}