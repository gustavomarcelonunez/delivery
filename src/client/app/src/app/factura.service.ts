import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { Factura } from './factura';
import { DataPackage } from './data-package';

@Injectable({
    providedIn: 'root'
})
export class FacturaService {

    private facturasUrl = 'rest/facturas';  // URL to web api

    constructor(private http: HttpClient, ) { }

    all(): Observable<DataPackage> {
        return this.http.get<DataPackage>(this.facturasUrl);
    }

    get(id: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.facturasUrl}/${id}`);
    }

    save(id: number, fechaPago: Date): Observable<DataPackage> {
        return this.http['put']<DataPackage>(`${this.facturasUrl}/${id}`, fechaPago);
    }

    generar(fechaEmision: Date): Observable<DataPackage> {
        return this.http['post']<DataPackage>(`${this.facturasUrl}`, fechaEmision);
    }

    // remove(id: number): Observable<DataPackage> {
    //     return this.http['delete']<DataPackage>(`${this.facturasUrl}/${id}`);
    // }

    byPage(page: number, cant: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.facturasUrl}?page=${page}&cant=${cant}`);
    }

    searchTypes(text: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.facturasUrl}/types/${text}`);
    }

    search(text: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.facturasUrl}/search/${text}`);
    }

    articulosByPage(id: number, page: number, cant: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.facturasUrl}/${id}/articulospedido?page=${page}&cant=${cant}`);
    }
}