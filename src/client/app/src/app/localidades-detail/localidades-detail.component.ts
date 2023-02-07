import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, tap, switchMap, map } from 'rxjs/operators';

import { Localidad } from '../localidad';
import { LocalidadService } from '../localidad.service'
import { Provincia } from '../provincia';
import { ProvinciaService } from '../provincia.service'
import { PaisService } from '../pais.service'

@Component({
    selector: 'app-localidades-detail',
    templateUrl: './localidades-detail.component.html',
    styleUrls: ['./localidades-detail.component.css']
})
export class LocalidadesDetailComponent implements OnInit {

    localidad: Localidad;
    searching: boolean = false;
    searchFailed: boolean = false;

    constructor(
        private route: ActivatedRoute,
        private localidadService: LocalidadService,
        private provinciaService: ProvinciaService,
        private paisService: PaisService,
        private location: Location
    ) {
    }

    ngOnInit() {
        this.get();
    }

    get(): void {
        const id = this.route.snapshot.paramMap.get('id');
        if (id === 'new') {
            this.localidad = <Localidad>{
                id: null,
                nombre: null,
                provincia: <Provincia>{},
            };
        } else {
            this.localidadService.get(+id)
                .subscribe(dataPackage => this.localidad = <Localidad>dataPackage.data
                );
        }
    }

    goBack(): void {
        this.location.back();
    }

    save(): void {
        this.localidadService.save(this.localidad)
            .subscribe(dataPackage => {
                this.localidad = <Localidad>dataPackage.data;
                this.goBack();
            });
    }

    searchType = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            tap(() => this.searching = true),
            switchMap(term =>
                this.localidadService.searchTypes(term)
                    .pipe(
                        map(response =>
                            response.data
                        )
                    )
                    .pipe(
                        tap(() => this.searchFailed = false),
                        catchError(() => {
                            this.searchFailed = true;
                            return of([]);
                        }))
            ),
            tap(() => this.searching = false)
        )
        searchProvincia = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            tap(() => (this.searching = true)),
            switchMap((term) =>
                this.provinciaService
                    .search(term)
                    .pipe(map((response) => response.data))
                    .pipe(
                        tap(() => (this.searchFailed = false)),
                        catchError(() => {
                            this.searchFailed = true;
                            return of([]);
                        })
                    )
            ),
            tap(() => (this.searching = false))
        );

        searchPais = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            tap(() => (this.searching = true)),
            switchMap((term) =>
                this.paisService
                    .search(term)
                    .pipe(map((response) => response.data))
                    .pipe(
                        tap(() => (this.searchFailed = false)),
                        catchError(() => {
                            this.searchFailed = true;
                            return of([]);
                        })
                    )
            ),
            tap(() => (this.searching = false))
        );


    resultFormat(value: any) {
        return value.nombre;
    }

    inputFormat(value: any) {
        if (value) return value.nombre;
        return null;
    }

}
