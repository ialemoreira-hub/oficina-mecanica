import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css'
})
export class Sidebar implements OnInit {
  usuario: any = null;
  inicial = '';
  perfil = '';

  get isAdmin(): boolean { return this.perfil === 'ADMINISTRADOR'; }
  get isAtendente(): boolean { return this.perfil === 'ATENDENTE'; }
  get isMecanico(): boolean { return this.perfil === 'MECANICO'; }

  // O que cada perfil pode ver:
  get verDashboard(): boolean { return this.isAdmin || this.isAtendente; }
  get verClientes(): boolean { return this.isAdmin || this.isAtendente; }
  get verVeiculos(): boolean { return this.isAdmin || this.isAtendente; }
  get verOrdens(): boolean { return true; } // todos veem
  get verRelatorios(): boolean { return this.isAdmin || this.isAtendente; }
  get verUsuarios(): boolean { return this.isAdmin; } // só admin

  constructor(private router: Router) {}

  ngOnInit() {
    const dados = localStorage.getItem('usuario');
    if (dados) {
      this.usuario = JSON.parse(dados);
      this.inicial = this.usuario.nome?.charAt(0).toUpperCase() || 'A';
      this.perfil = this.usuario.perfil?.toUpperCase() || '';
    }
  }

  sair() {
    localStorage.removeItem('usuario');
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}