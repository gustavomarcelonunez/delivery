import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, tap, switchMap, map } from 'rxjs/operators';

import { Provincia } from '../provincia';
import { ProvinciaService } from '../provincia.service';
import { Pais } from '../pais';
import { PaisService } from '../pais.service'

@Component({
    selector: 'app-provincias-detail',
    templateUrl: './provincias-detail.component.html',
    styleUrls: ['./provincias-detail.component.css']
})
export class ProvinciasDetailComponent implements OnInit {

    provincia: Provincia;
    searching: boolean = false;
    searchFailed: boolean = false;

    constructor(
        private route: ActivatedRoute,
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
            this.provincia = <Provincia>{
                id: null,
                nombre: null,
                pais: <Pais>{},

            };
        } else {
            this.provinciaService.get(+id)
                .subscribe(dataPackage => this.provincia = <Provincia>dataPackage.data
                );
        }
    }

    goBack(): void {
        this.location.back();
    }

    save(): void {
        this.provinciaService.save(this.provincia)
            .subscribe(dataPackage => {
                this.provincia = <Provincia>dataPackage.data;
                this.goBack();
            });
    }

    searchType = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            tap(() => this.searching = true),
            switchMap(term =>
                this.provinciaService.searchTypes(term)
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
