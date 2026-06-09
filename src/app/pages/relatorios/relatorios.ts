import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { forkJoin } from 'rxjs';
import { Sidebar } from '../../layout/sidebar/sidebar';

@Component({
  selector: 'app-relatorios',
  standalone: true,
  imports: [CommonModule, Sidebar],
  templateUrl: './relatorios.html',
  styleUrl: './relatorios.css'
})
export class Relatorios implements OnInit {

  stats = {
    totalOs: 0,
    osAbertas: 0,
    osConcluidas: 0,
    totalClientes: 0
  };

  statusCount: any[] = [];
  ultimasOs: any[] = [];
  carregando = true;
  erro = '';

  statusLabels: any = {
    'ABERTA': 'Aberta',
    'EM_ANDAMENTO': 'Em Andamento',
    'AGUARDANDO_PECA': 'Aguardando Peça',
    'CONCLUIDA': 'Concluída',
    'ENTREGUE': 'Entregue',
    'CANCELADA': 'Cancelada'
  };

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.carregarDados();
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      'Content-Type': 'application/json',
      ...(token ? { 'Authorization': `Bearer ${token}` } : {})
    });
  }

  private toArray(dados: any): any[] {
    return Array.isArray(dados) ? dados : (dados.content ?? []);
  }

  carregarDados() {
    this.carregando = true;
    this.erro = '';

    forkJoin({
      ordens: this.http.get<any>('https://oficina-mecanica-wd4c.onrender.com/ordens/api', { headers: this.getHeaders() }),
      clientes: this.http.get<any>('https://oficina-mecanica-wd4c.onrender.com/clientes/api', { headers: this.getHeaders() })
    }).subscribe({
      next: ({ ordens, clientes }) => {
        const listaOrdens = this.toArray(ordens);
        const listaClientes = this.toArray(clientes);

        this.stats.totalOs = listaOrdens.length;
        this.stats.osAbertas = listaOrdens.filter((os: any) => os.status === 'ABERTA').length;
        this.stats.osConcluidas = listaOrdens.filter((os: any) => os.status === 'CONCLUIDA').length;
        this.stats.totalClientes = listaClientes.length;

        const contagem: any = {};
        listaOrdens.forEach((os: any) => {
          contagem[os.status] = (contagem[os.status] || 0) + 1;
        });
        this.statusCount = Object.keys(contagem).map(s => ({
          status: s,
          label: this.statusLabels[s] || s,
          count: contagem[s]
        }));

        this.ultimasOs = listaOrdens
          .slice(-10)
          .reverse()
          .map((os: any) => ({ ...os, statusLabel: this.statusLabels[os.status] || os.status }));

        this.carregando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.erro = 'Não foi possível carregar os dados.';
        this.carregando = false;
        this.cdr.detectChanges();
      }
    });
  }
}
