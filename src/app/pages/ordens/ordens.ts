import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Sidebar } from '../../layout/sidebar/sidebar';

@Component({
  selector: 'app-ordens',
  standalone: true,
  imports: [CommonModule, FormsModule, Sidebar],
  templateUrl: './ordens.html',
  styleUrl: './ordens.css'
})
export class Ordens implements OnInit {

  private api = 'https://oficina-mecanica-wd4c.onrender.com/ordens/api';
  private apiVeiculos = 'https://oficina-mecanica-wd4c.onrender.com/veiculos/api';

  ordens: any[] = [];
  ordensFiltradas: any[] = [];
  veiculos: any[] = [];
  filtroStatus = '';
  carregando = true;
  erro = '';
  modalAberto = false;
  osDetalhe: any = null;
  salvando = false;

  form: any = { veiculoId: '', descricaoProblema: '' };

  novoItem: any = {
    descricao: '',
    tipo: 'PECA',
    quantidade: 1,
    valorUnitario: 0
  };

  statusOpcoes = [
    { value: '', label: 'Todos' },
    { value: 'ABERTA', label: 'Aberta' },
    { value: 'EM_ANDAMENTO', label: 'Em Andamento' },
    { value: 'AGUARDANDO_PECA', label: 'Aguardando Peça' },
    { value: 'CONCLUIDA', label: 'Concluída' },
    { value: 'ENTREGUE', label: 'Entregue' },
    { value: 'CANCELADA', label: 'Cancelada' }
  ];

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.carregarOrdens();
    this.carregarVeiculos();
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      'Content-Type': 'application/json',
      ...(token ? { 'Authorization': `Bearer ${token}` } : {})
    });
  }

  getStatusLabel(status: string): string {
    const labels: any = {
      'ABERTA': 'Aberta',
      'EM_ANDAMENTO': 'Em Andamento',
      'AGUARDANDO_PECA': 'Aguardando Peça',
      'CONCLUIDA': 'Concluída',
      'ENTREGUE': 'Entregue',
      'CANCELADA': 'Cancelada'
    };
    return labels[status] || status;
  }

  carregarOrdens() {
    this.carregando = true;
    this.http.get<any>(this.api, { headers: this.getHeaders() }).subscribe({
      next: (dados) => {
        const lista = Array.isArray(dados) ? dados : (dados.content ?? []);
        this.ordens = lista.map((os: any) => ({
          ...os,
          statusLabel: this.getStatusLabel(os.status)
        }));
        this.filtrarStatus('');
        this.carregando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.erro = 'Erro ao carregar ordens.';
        this.carregando = false;
        this.cdr.detectChanges();
      }
    });
  }

  carregarVeiculos() {
    this.http.get<any>(this.apiVeiculos, { headers: this.getHeaders() }).subscribe({
      next: (dados) => {
        this.veiculos = Array.isArray(dados) ? dados : (dados.content ?? []);
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Erro:', err)
    });
  }

  filtrarStatus(status: string) {
    this.filtroStatus = status;
    this.ordensFiltradas = status
      ? this.ordens.filter(os => os.status === status)
      : [...this.ordens];
    this.cdr.detectChanges();
  }

  abrirModal() {
    this.erro = '';
    this.form = { veiculoId: '', descricaoProblema: '' };
    this.modalAberto = true;
  }

  fecharModal() {
    this.modalAberto = false;
    this.salvando = false;
    this.erro = '';
  }

  salvar() {
    if (this.salvando) return;
    if (!this.form.veiculoId) { this.erro = 'Selecione um veículo.'; return; }
    this.erro = '';
    this.salvando = true;
    this.http.post(this.api, this.form, { headers: this.getHeaders() }).subscribe({
      next: () => { this.fecharModal(); this.carregarOrdens(); },
      error: () => { this.erro = 'Erro ao abrir OS.'; this.salvando = false; }
    });
  }

  verDetalhe(os: any) {
    this.http.get<any>(`${this.api}/${os.id}`, { headers: this.getHeaders() }).subscribe({
      next: (data) => {
        this.osDetalhe = { ...data, statusLabel: this.getStatusLabel(data.status) };
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Erro:', err)
    });
  }

  fecharDetalhe() {
    this.osDetalhe = null;
    this.carregarOrdens();
  }

  atualizarStatus(id: number, status: string) {
    this.http.post<any>(`${this.api}/${id}/status`, { status }, { headers: this.getHeaders() }).subscribe({
      next: (data) => {
        this.osDetalhe = { ...data, statusLabel: this.getStatusLabel(data.status) };
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Erro:', err)
    });
  }

  adicionarItem() {
  if (!this.novoItem.descricao || !this.novoItem.valorUnitario) return;
  this.http.post<any>(`${this.api}/${this.osDetalhe.id}/itens`, this.novoItem, { headers: this.getHeaders() }).subscribe({
    next: (data) => {
      this.osDetalhe = { ...data, statusLabel: this.getStatusLabel(data.status) };
      this.novoItem = { descricao: '', tipo: 'PECA', quantidade: 1, valorUnitario: 0 };
      this.cdr.detectChanges();
    },
    error: (err) => console.error('Erro:', err)
  });
}
}
