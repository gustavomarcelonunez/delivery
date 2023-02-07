import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { Remito } from './remito';
import { DataPackage } from './data-package';

@Injectable({
    providedIn: 'root'
})
export class RemitoService {

    private remitosUrl = 'rest/remitos';  // URL to web api

    constructor(private http: HttpClient, ) { }

    all(): Observable<DataPackage> {
        return this.http.get<DataPackage>(this.remitosUrl);
    }

    get(id: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.remitosUrl}/${id}`);
    }

    save(remito: Remito): Observable<DataPackage> {
        return this.http['put']<DataPackage>(this.remitosUrl, remito);
    }

    generar(fechaArmado: Date): Observable<DataPackage> {
        return this.http['post']<DataPackage>(`${this.remitosUrl}`, fechaArmado);
    }

    // remove(id: number): Observable<DataPackage> {
    //     return this.http['delete']<DataPackage>(`${this.remitosUrl}/${id}`);
    // }

    byPage(page: number, cant: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.remitosUrl}?page=${page}&cant=${cant}`);
    }

    articulosByPage(id: number, page: number, cant: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.remitosUrl}/${id}/articulospedido?page=${page}&cant=${cant}`);
    }

    searchTypes(text: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.remitosUrl}/types/${text}`);
    }

    search(text: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.remitosUrl}/search/${text}`);
    }
}