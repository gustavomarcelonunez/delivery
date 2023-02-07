import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { Provincia } from './provincia';
import { DataPackage } from './data-package';

@Injectable({
  providedIn: 'root'
})
export class ProvinciaService {

  private provinciasUrl = 'rest/provincias';

  constructor(private http: HttpClient, ) { }

  all(): Observable<DataPackage> {
    return this.http.get<DataPackage>(this.provinciasUrl);
  }

  get(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.provinciasUrl}/${id}`);
  }

  save(provincia: Provincia): Observable<DataPackage> {
    return this.http[provincia.id? 'put' : 'post']<DataPackage>(this.provinciasUrl, provincia);
  }

  remove(id: number): Observable<DataPackage> {
    return this.http['delete']<DataPackage>(`${this.provinciasUrl}/${id}`);
  }

  byPage(page: number, cant: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.provinciasUrl}?page=${page}&cant=${cant}`);
  }

  searchTypes(text: string): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.provinciasUrl}/types/${text}`);
  }

  search(text: string): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.provinciasUrl}/search/${text}`);
  }
}