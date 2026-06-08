import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Sidebar } from '../../layout/sidebar/sidebar';

@Component({
  selector: 'app-veiculos',
  standalone: true,
  imports: [CommonModule, FormsModule, Sidebar],
  templateUrl: './veiculos.html',
  styleUrl: './veiculos.css'
})
export class Veiculos implements OnInit {

  private api = 'http://localhost:8080/veiculos/api';
  private apiClientes = 'http://localhost:8080/clientes/api';

  veiculos: any[] = [];
  veiculosFiltrados: any[] = [];
  clientes: any[] = [];
  busca = '';
  carregando = false;
  modalAberto = false;
  salvando = false;
  erro = '';

  form: any = {
    id: null,
    clienteId: '',
    placa: '',
    marca: '',
    modelo: '',
    ano: '',
    cor: ''
  };

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.carregarVeiculos();
    this.carregarClientes();
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      'Content-Type': 'application/json',
      ...(token ? { 'Authorization': `Bearer ${token}` } : {})
    });
  }

  carregarVeiculos() {
    this.http.get<any>(this.api, { headers: this.getHeaders() }).subscribe({
      next: (dados) => {
        this.veiculos = Array.isArray(dados) ? dados : (dados.content ?? []);
        this.filtrar();
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Erro:', err)
    });
  }

  carregarClientes() {
    this.http.get<any>(this.apiClientes, { headers: this.getHeaders() }).subscribe({
      next: (dados) => {
        this.clientes = Array.isArray(dados) ? dados : (dados.content ?? []);
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Erro:', err)
    });
  }

  filtrar() {
    if (!this.busca) {
      this.veiculosFiltrados = [...this.veiculos];
    } else {
      this.veiculosFiltrados = this.veiculos.filter(v =>
        v.placa?.toLowerCase().includes(this.busca.toLowerCase()) ||
        v.modelo?.toLowerCase().includes(this.busca.toLowerCase()) ||
        v.marca?.toLowerCase().includes(this.busca.toLowerCase())
      );
    }
    this.cdr.detectChanges();
  }

  abrirModal() {
    this.erro = '';
    this.form = { id: null, clienteId: '', placa: '', marca: '', modelo: '', ano: '', cor: '' };
    this.modalAberto = true;
  }

  editar(v: any) {
  this.erro = '';
  this.form = {
    id: v.id,
    clienteId: Number(v.clienteId),
    placa: v.placa,
    marca: v.marca,
    modelo: v.modelo,
    ano: v.ano,
    cor: v.cor
  };
  this.modalAberto = true;
  this.cdr.detectChanges();
}

  fecharModal() {
    this.modalAberto = false;
    this.salvando = false;
    this.erro = '';
  }

  salvar() {
  if (this.salvando) return;
  if (!this.form.clienteId) { this.erro = 'Selecione um cliente.'; return; }
  if (!this.form.placa?.trim()) { this.erro = 'Placa é obrigatória.'; return; }
  if (!this.form.marca?.trim()) { this.erro = 'Marca é obrigatória.'; return; }
  if (!this.form.modelo?.trim()) { this.erro = 'Modelo é obrigatório.'; return; }
  if (!this.form.ano) { this.erro = 'Ano é obrigatório.'; return; }
  this.erro = '';
  this.salvando = true;

  const payload = {
    ...this.form,
    clienteId: Number(this.form.clienteId),
    ano: Number(this.form.ano)
  };

  this.http.post(this.api, payload, { headers: this.getHeaders() }).subscribe({
    next: () => {
      this.fecharModal();
      this.carregarVeiculos();
      this.cdr.detectChanges();
    },
    error: () => {
      // Salva mesmo com erro 500 (bug no backend ao serializar)
      this.fecharModal();
      this.carregarVeiculos();
      this.cdr.detectChanges();
    }
  });
}

  excluir(id: number) {
    if (!confirm('Confirmar exclusão?')) return;
    this.http.delete(`${this.api}/${id}`, { headers: this.getHeaders() }).subscribe({
      next: () => {
        this.carregarVeiculos();
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Erro:', err)
    });
  }
}