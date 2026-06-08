import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  email = '';
  senha = '';
  erro = '';
  carregando = false;

  constructor(private router: Router, private http: HttpClient) {}

  login() {
    this.erro = '';
    this.carregando = true;

    this.http.post<any>('http://localhost:8080/usuarios/api/login', {
      email: this.email,
      senha: this.senha
    }).subscribe({
      next: (usuario) => {
        localStorage.setItem('usuario', JSON.stringify(usuario));
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        // Fallback para admin local
        if (this.email === 'admin@oficina.com' && this.senha === 'admin123') {
          localStorage.setItem('usuario', JSON.stringify({
            nome: 'Administrador',
            email: this.email,
            perfil: 'ADMINISTRADOR'
          }));
          this.router.navigate(['/dashboard']);
        } else {
          this.erro = 'E-mail ou senha incorretos.';
          this.carregando = false;
        }
      }
    });
  }
}