import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Sidebar } from '../../layout/sidebar/sidebar';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule, Sidebar, NgxMaskDirective],
  providers: [provideNgxMask()],
  templateUrl: './clientes.html',
  styleUrl: './clientes.css'
})
export class Clientes implements OnInit {

  private api = 'https://oficina-mecanica-wd4c.onrender.com/clientes/api';

  clientes: any[] = [];
  carregando = true;
  erro = '';
  busca = '';

  modalAberto = false;
  editando = false;
  salvando = false;
  form: any = {};

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.carregar();
  }

  get clientesFiltrados(): any[] {
    if (!this.busca) return this.clientes;
    return this.clientes.filter(c =>
      c.nome?.toLowerCase().includes(this.busca.toLowerCase()) ||
      c.cpfCnpj?.includes(this.busca)
    );
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      'Content-Type': 'application/json',
      ...(token ? { 'Authorization': `Bearer ${token}` } : {})
    });
  }

  carregar() {
    this.carregando = true;
    this.http.get<any>(this.api, { headers: this.getHeaders() }).subscribe({
      next: (dados) => {
        this.clientes = Array.isArray(dados) ? dados : (dados.content ?? []);
        this.carregando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.erro = 'Erro ao carregar clientes.';
        this.carregando = false;
        this.cdr.detectChanges();
      }
    });
  }

  abrirModal(cliente?: any) {
    this.form = cliente ? { ...cliente } : {};
    this.editando = !!cliente;
    this.modalAberto = true;
    this.erro = '';
    this.cdr.detectChanges();
  }

  fecharModal() {
    this.modalAberto = false;
    this.form = {};
    this.erro = '';
    this.cdr.detectChanges();
  }

  salvar() {
    if (!this.form.nome?.trim()) { this.erro = 'Nome é obrigatório.'; return; }
    this.salvando = true;
    this.cdr.detectChanges();
    const req = this.editando
      ? this.http.put(`${this.api}/${this.form.id}`, this.form, { headers: this.getHeaders() })
      : this.http.post(this.api, this.form, { headers: this.getHeaders() });

    req.subscribe({
      next: () => {
        this.fecharModal();
        this.salvando = false;
        this.carregar();
        this.cdr.detectChanges();
      },
      error: () => {
        this.erro = 'Erro ao salvar.';
        this.salvando = false;
        this.cdr.detectChanges();
      }
    });
  }

  excluir(id: number) {
    if (!confirm('Confirmar exclusão?')) return;
    this.http.delete(`${this.api}/${id}`, { headers: this.getHeaders() }).subscribe({
      next: () => {
        this.carregar();
        this.cdr.detectChanges();
      }
    });
  }
}
