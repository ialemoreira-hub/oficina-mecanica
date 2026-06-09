import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Sidebar } from '../../layout/sidebar/sidebar';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, Sidebar],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {

  stats = {
    totalOs: 0,
    osAbertas: 0,
    totalClientes: 0,
    totalVeiculos: 0
  };

  ultimasOs: any[] = [];

  constructor(private router: Router, private http: HttpClient, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    const usuario = localStorage.getItem('usuario');
    if (!usuario) {
      this.router.navigate(['/login']);
      return;
    }
    this.carregarDados();
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      'Content-Type': 'application/json',
      ...(token ? { 'Authorization': `Bearer ${token}` } : {})
    });
  }

  carregarDados() {
    forkJoin({
      ordens: this.http.get<any>('https://oficina-mecanica-wd4c.onrender.com/ordens/api', { headers: this.getHeaders() }),
      clientes: this.http.get<any>('https://oficina-mecanica-wd4c.onrender.com/clientes/api', { headers: this.getHeaders() }),
      veiculos: this.http.get<any>('https://oficina-mecanica-wd4c.onrender.com/veiculos/api', { headers: this.getHeaders() })
    }).subscribe({
      next: ({ ordens, clientes, veiculos }) => {
        const listaOrdens = Array.isArray(ordens) ? ordens : (ordens.content ?? []);
        const listaClientes = Array.isArray(clientes) ? clientes : (clientes.content ?? []);
        const listaVeiculos = Array.isArray(veiculos) ? veiculos : (veiculos.content ?? []);

        this.stats.totalOs = listaOrdens.length;
        this.stats.osAbertas = listaOrdens.filter((os: any) => os.status === 'ABERTA').length;
        this.stats.totalClientes = listaClientes.length;
        this.stats.totalVeiculos = listaVeiculos.length;

        this.ultimasOs = listaOrdens.slice(-5).reverse();
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Erro ao carregar dashboard:', err)
    });
  }

  novaOs() {
    this.router.navigate(['/ordens']);
  }
}
