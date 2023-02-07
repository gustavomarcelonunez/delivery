import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { Articulo } from './articulo';
import { DataPackage } from './data-package';

@Injectable({
    providedIn: 'root'
})
export class ArticuloService {

    private articulosUrl = 'rest/articulos';  // URL to web api

    constructor(private http: HttpClient, ) { }

    all(): Observable<DataPackage> {
        return this.http.get<DataPackage>(this.articulosUrl);
    }

    get(codigo: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.articulosUrl}/${codigo}`);
    }

    save(articulo: Articulo): Observable<DataPackage> {
        return this.http[articulo.id ? 'put' : 'post']<DataPackage>(`${this.articulosUrl}/${articulo.id}`, articulo);
    }

    remove(id: number): Observable<DataPackage> {
        return this.http['delete']<DataPackage>(`${this.articulosUrl}/${id}`);
    }

    byPage(page: number, cant: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.articulosUrl}?page=${page}&cant=${cant}`);
    }

    searchTypes(text: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.articulosUrl}/types/${text}`);
    }

    search(text: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.articulosUrl}/search/${text}`);
    }
}