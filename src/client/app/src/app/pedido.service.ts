import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { Pedido } from './pedido';
import { DataPackage } from './data-package';

@Injectable({
    providedIn: 'root'
})
export class PedidoService {

    private pedidosUrl = 'rest/pedidos';  // URL to web api

    constructor(private http: HttpClient, ) { }

    all(): Observable<DataPackage> {
        return this.http.get<DataPackage>(this.pedidosUrl);
    }

    get(id: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.pedidosUrl}/${id}`);
      }

    save(pedido: Pedido): Observable<DataPackage> {
        for (let index = 0; index < pedido.articulosPedido.length; index++) {
            pedido.articulosPedido[index].pedido = null;     
        }
        return this.http[pedido.id ? 'put' : 'post']<DataPackage>(`${this.pedidosUrl}/${pedido.id}`, pedido);
    }

    // remove(id: number): Observable<DataPackage> {
    //     return this.http['delete']<DataPackage>(`${this.pedidosUrl}/${id}`);
    // }

    byPage(page: number, cant: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.pedidosUrl}?page=${page}&cant=${cant}`);
    }

    searchTypes(text: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.pedidosUrl}/types/${text}`);
    }

    search(text: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.pedidosUrl}/search/${text}`);
    }
}