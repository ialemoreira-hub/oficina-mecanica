import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Dashboard } from './pages/dashboard/dashboard';
import { Clientes } from './pages/clientes/clientes';
import { Veiculos } from './pages/veiculos/veiculos';
import { Ordens } from './pages/ordens/ordens';
import { Relatorios } from './pages/relatorios/relatorios';
import { Usuarios } from './pages/usuarios/usuarios';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'dashboard', component: Dashboard },
  { path: 'clientes', component: Clientes },
  { path: 'veiculos', component: Veiculos },
  { path: 'ordens', component: Ordens },
  { path: 'relatorios', component: Relatorios },
  { path: 'usuarios', component: Usuarios },
];