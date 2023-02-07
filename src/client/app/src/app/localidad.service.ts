import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { Localidad } from './localidad';
import { DataPackage } from './data-package';

@Injectable({
    providedIn: 'root'
})
export class LocalidadService {

    private localidadesUrl = 'rest/localidades';

    constructor(private http: HttpClient, ) { }

    all(): Observable<DataPackage> {
        return this.http.get<DataPackage>(this.localidadesUrl);
    }

    get(id: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.localidadesUrl}/${id}`);
    }

    save(localidad: Localidad): Observable<DataPackage> {
        return this.http[localidad.id ? 'put' : 'post']<DataPackage>(this.localidadesUrl, localidad);
    }

    remove(id: number): Observable<DataPackage> {
        return this.http['delete']<DataPackage>(`${this.localidadesUrl}/${id}`);
    }

    byPage(page: number, cant: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.localidadesUrl}?page=${page}&cant=${cant}`);
    }

    searchTypes(text: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.localidadesUrl}/types/${text}`);
    }

    search(text: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.localidadesUrl}/search/${text}`);
    }
}