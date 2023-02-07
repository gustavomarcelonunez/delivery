import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule }    from '@angular/common/http';
import { HomeComponent } from './home/home.component';
import { ClientesComponent } from './clientes/clientes.component';
import { ClientesDetailComponent } from './clientes-detail/clientes-detail.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgbdModalConfirm } from './modal-component';
import { ArticulosComponent } from './articulos/articulos.component';
import { ArticulosDetailComponent } from './articulos-detail/articulos-detail.component';
import { PedidosComponent } from './pedidos/pedidos.component';
import { LocalidadesComponent } from './localidades/localidades.component';
import { ProvinciasComponent } from './provincias/provincias.component';
import { PaisesComponent } from './paises/paises.component';
import { DomiciliosEntregaComponent } from './domicilios-entrega/domicilios-entrega.component';
import { LocalidadesDetailComponent } from './localidades-detail/localidades-detail.component';
import { PaisesDetailComponent } from './paises-detail/paises-detail.component';
import { ProvinciasDetailComponent } from './provincias-detail/provincias-detail.component';
import { DomiciliosEntregaDetailComponent } from './domicilios-entrega-detail/domicilios-entrega-detail.component';
import { PedidosDetailComponent } from './pedidos-detail/pedidos-detail.component';
import { RemitosComponent } from './remitos/remitos.component';
import { RemitosDetailComponent } from './remitos-detail/remitos-detail.component';
import { FacturasComponent } from './facturas/facturas.component';
import { FacturasDetailComponent } from './facturas-detail/facturas-detail.component';
import { ArticulosPedidoComponent } from './articulospedido/articulospedido.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ArticulosComponent,
    ArticulosDetailComponent,
    NgbdModalConfirm,
    ClientesDetailComponent,
    ClientesComponent,
    PedidosComponent,
    LocalidadesComponent,
    ProvinciasComponent,
    PaisesComponent,
    DomiciliosEntregaComponent,
    LocalidadesDetailComponent,
    PaisesDetailComponent,
    ProvinciasDetailComponent,
    DomiciliosEntregaDetailComponent,
    PedidosDetailComponent,
    RemitosComponent,
    RemitosDetailComponent,
    FacturasComponent,
    FacturasDetailComponent,
    ArticulosPedidoComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgbModule,
  ],
  entryComponents: [
    NgbdModalConfirm
  ],
  providers: [],
  bootstrap: [AppComponent],

})
export class AppModule { }
