package com.oficina.service;

import com.oficina.model.Usuario;
import com.oficina.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Integra nosso sistema de usuários com o Spring Security.
 * O Spring Security usa este serviço para autenticar o login.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        if (!usuario.isAtivo()) {
            throw new UsernameNotFoundException("Usuário inativo: " + email);
        }

        // POLIMORFISMO: getRole() retorna o papel correto dependendo do tipo real do objeto
        // Se for Administrador → "ROLE_ADMIN"
        // Se for Atendente    → "ROLE_ATENDENTE"
        // Se for Mecanico     → "ROLE_MECANICO"
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getSenha(),
                List.of(new SimpleGrantedAuthority(usuario.getRole()))
        );
    }
}
