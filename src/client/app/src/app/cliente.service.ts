import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { Cliente } from './cliente';
import { DataPackage } from './data-package';

@Injectable({
    providedIn: 'root'
})
export class ClienteService {

    private clientesUrl = 'rest/clientes';  // URL to web api

    constructor(private http: HttpClient, ) { }

    all(): Observable<DataPackage> {
        return this.http.get<DataPackage>(this.clientesUrl);
    }

    get(cuit: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.clientesUrl}/${cuit}`);
    }

    save(cliente: Cliente): Observable<DataPackage> {
        return this.http[cliente.cuit ? 'put' : 'post']<DataPackage>(this.clientesUrl, cliente);
    }

    remove(id: number): Observable<DataPackage> {
        return this.http['delete']<DataPackage>(`${this.clientesUrl}/${id}`);
    }

    byPage(page: number, cant: number): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.clientesUrl}?page=${page}&cant=${cant}`);
    }

    searchTypes(text: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.clientesUrl}/types/${text}`);
    }

    search(text: string): Observable<DataPackage> {
        return this.http.get<DataPackage>(`${this.clientesUrl}/search/${text}`);
    }
}