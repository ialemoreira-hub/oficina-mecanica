import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Sidebar } from '../../layout/sidebar/sidebar';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule, Sidebar],
  templateUrl: './usuarios.html',
  styleUrl: './usuarios.css'
})
export class Usuarios implements OnInit {
  usuarios: any[] = [];
  modalAberto = false;
  salvando = false;
  erro = '';

  form: any = {
    tipo: '',
    nome: '',
    email: '',
    senha: '',
    matricula: '',
    especialidade: ''
  };

  constructor(private router: Router) {}

  ngOnInit() {
    const usuario = localStorage.getItem('usuario');
    if (!usuario) { this.router.navigate(['/login']); return; }
    this.carregarUsuarios();
  }

  carregarUsuarios() {
    fetch('http://localhost:8080/usuarios/api')
      .then(r => r.json())
      .then((data: any[]) => {
        this.usuarios = data;
      })
      .catch(err => console.error('Erro:', err));
  }

  onTipoChange() {
    this.form.matricula = '';
    this.form.especialidade = '';
  }

  abrirModal() {
    this.erro = '';
    this.form = { tipo: '', nome: '', email: '', senha: '', matricula: '', especialidade: '' };
    this.modalAberto = true;
  }

  fecharModal() {
    this.modalAberto = false;
    this.salvando = false;
    this.erro = '';
  }

  salvar() {
    if (this.salvando) return;
    this.erro = '';
    this.salvando = true;

    fetch('http://localhost:8080/usuarios/api', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(this.form)
    })
    .then(r => {
      if (!r.ok) return r.text().then(msg => { throw new Error(msg); });
      return r.json();
    })
    .then(() => {
      this.fecharModal();
      this.carregarUsuarios();
    })
    .catch(err => {
      this.erro = 'Erro ao cadastrar. Verifique os dados.';
      this.salvando = false;
    });
  }

  desativar(id: number) {
    if (!confirm('Desativar este usuário?')) return;
    fetch(`http://localhost:8080/usuarios/api/${id}`, { method: 'DELETE' })
      .then(() => this.carregarUsuarios())
      .catch(err => console.error('Erro:', err));
  }
}