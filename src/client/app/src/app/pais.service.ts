import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { Pais } from './pais';
import { DataPackage } from './data-package';

@Injectable({
    providedIn: 'root'
})
export class PaisService {

    private paisesUrl = 'rest/paises';

    constructor(private http: HttpClient, ) { }

    all(): Observable<DataPackage> {
        return this.http.get<DataPackage>(this.paisesUrl);
    }

    get(id: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.paisesUrl}/${id}`);
    }

    save(pais: Pais): Observable<DataPackage> {
        return this.http[pais.id ? 'put' : 'post']<DataPackage>(this.paisesUrl, pais);
    }

    remove(id: number): Observable<DataPackage> {
        return this.http['delete']<DataPackage>(`${this.paisesUrl}/${id}`);
    }

    byPage(page: number, cant: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.paisesUrl}?page=${page}&cant=${cant}`);
    }

    searchTypes(text: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.paisesUrl}/types/${text}`);
    }

    search(text: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.paisesUrl}/search/${text}`);
    }
}