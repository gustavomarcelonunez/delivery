import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ClientesComponent } from './clientes/clientes.component'
import { ClientesDetailComponent } from './clientes-detail/clientes-detail.component'
import { ArticulosComponent } from './articulos/articulos.component'
import { ArticulosDetailComponent } from './articulos-detail/articulos-detail.component'
import { ArticulosPedidoComponent } from './articulospedido/articulospedido.component'
import { PedidosComponent } from './pedidos/pedidos.component'
import { PedidosDetailComponent } from './pedidos-detail/pedidos-detail.component'
import { DomiciliosEntregaComponent } from './domicilios-entrega/domicilios-entrega.component'
import { DomiciliosEntregaDetailComponent } from './domicilios-entrega-detail/domicilios-entrega-detail.component'
import { LocalidadesComponent } from './localidades/localidades.component'
import { LocalidadesDetailComponent } from './localidades-detail/localidades-detail.component'
import { ProvinciasComponent } from './provincias/provincias.component'
import { ProvinciasDetailComponent } from './provincias-detail/provincias-detail.component'
import { PaisesComponent } from './paises/paises.component'
import { PaisesDetailComponent } from './paises-detail/paises-detail.component'
import { RemitosComponent } from './remitos/remitos.component'
import { RemitosDetailComponent } from './remitos-detail/remitos-detail.component'
import { FacturasComponent } from './facturas/facturas.component'
import { FacturasDetailComponent } from './facturas-detail/facturas-detail.component'

const routes: Routes = [ 
  { path: '', component: HomeComponent } ,
  { path: 'clientes', component: ClientesComponent },
  { path: 'clientes/:cuit', component: ClientesDetailComponent },
  { path: 'clientes/:cuit/articulospedido', component: ArticulosPedidoComponent },
  { path: 'articulos', component: ArticulosComponent },
  { path: 'articulos/:codigo', component: ArticulosDetailComponent },
  { path: 'pedidos', component: PedidosComponent },
  { path: 'pedidos/:id', component: PedidosDetailComponent },
  { path: 'domicilios', component: DomiciliosEntregaComponent },
  { path: 'domicilios/:id', component: DomiciliosEntregaDetailComponent },
  { path: 'localidades', component: LocalidadesComponent },
  { path: 'localidades/:id', component: LocalidadesDetailComponent },
  { path: 'provincias', component: ProvinciasComponent },
  { path: 'provincias/:id', component: ProvinciasDetailComponent },
  { path: 'paises', component: PaisesComponent },
  { path: 'paises/:id', component: PaisesDetailComponent },
  { path: 'remitos', component: RemitosComponent },
  { path: 'remitos/:id/articulospedido', component: RemitosDetailComponent },
  { path: 'facturas', component: FacturasComponent },
  { path: 'facturas/:id/articulospedido', component: FacturasDetailComponent },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
