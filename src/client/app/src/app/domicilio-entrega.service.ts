import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { DomicilioEntrega } from './domicilio-entrega';
import { DataPackage } from './data-package';

@Injectable({
  providedIn: 'root'
})
export class DomicilioEntregaService {

  private domiciliosUrl = 'rest/domicilios';

  constructor(private http: HttpClient, ) { }

  all(): Observable<DataPackage> {
    return this.http.get<DataPackage>(this.domiciliosUrl);
  }

  get(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.domiciliosUrl}/${id}`);
  }

  save(domicilioEntrega: DomicilioEntrega): Observable<DataPackage> {
    return this.http[domicilioEntrega.id? 'put' : 'post']<DataPackage>(this.domiciliosUrl, domicilioEntrega);
  }

  remove(id: number): Observable<DataPackage> {
    return this.http['delete']<DataPackage>(`${this.domiciliosUrl}/${id}`);
  }

  byPage(page: number, cant: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.domiciliosUrl}?page=${page}&cant=${cant}`);
  }

  searchTypes(text: string): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.domiciliosUrl}/types/${text}`);
  }

  search(text: string): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.domiciliosUrl}/search/${text}`);
  }
}