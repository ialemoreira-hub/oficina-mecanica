package com.oficina.util;

import com.oficina.model.Administrador;
import com.oficina.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Cria o usuário administrador padrão ao iniciar a aplicação,
 * se ainda não existir nenhum usuário no banco.
 *
 * Login padrão:
 *   E-mail: admin@oficina.com
 *   Senha:  admin123
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            Administrador admin = new Administrador(
                "Administrador",
                "admin@oficina.com",
                passwordEncoder.encode("admin123")
            );
            usuarioRepository.save(admin);
            System.out.println("==============================================");
            System.out.println("  Usuário admin criado com sucesso!");
            System.out.println("  E-mail: admin@oficina.com");
            System.out.println("  Senha:  admin123");
            System.out.println("==============================================");
        }
    }
}
