import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { ArticulosPedido } from './articulos-pedido';
import { DataPackage } from './data-package';

@Injectable({
    providedIn: 'root'
})
export class ArticulosPedidoService {

    private articulosPedidoUrl = 'rest/articulospedido';  // URL to web api
    private clientesUrl= 'rest/clientes';

    constructor(private http: HttpClient, ) { }

    all(): Observable<DataPackage> {
        return this.http.get<DataPackage>(this.articulosPedidoUrl);
    }

    get(id: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.articulosPedidoUrl}/${id}`);
    }

    getArticulosPedido(cuit: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.clientesUrl}/${cuit}/articulospedido`);
    }

    save(articulosPedido: ArticulosPedido): Observable<DataPackage> {
        return this.http[articulosPedido.id ? 'put' : 'post']<DataPackage>(this.articulosPedidoUrl, articulosPedido);
    }

    remove(id: number): Observable<DataPackage> {
        return this.http['delete']<DataPackage>(`${this.articulosPedidoUrl}/${id}`);
    }

    byPage(cuit: string, page: number, cant: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.clientesUrl}/${cuit}/articulospedido?page=${page}&cant=${cant}`);
    }

    searchTypes(text: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.articulosPedidoUrl}/types/${text}`);
    }

    search(text: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.articulosPedidoUrl}/search/${text}`);
    }
}