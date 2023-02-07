import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, tap, switchMap, map } from 'rxjs/operators';

import { DomicilioEntrega } from '../domicilio-entrega';
import { DomicilioEntregaService } from '../domicilio-entrega.service'
import { Localidad } from '../localidad'
import { LocalidadService } from '../localidad.service'
import { ModalService } from "../modal.service";

@Component({
    selector: 'app-domicilios-entrega-detail',
    templateUrl: './domicilios-entrega-detail.component.html',
    styleUrls: ['./domicilios-entrega-detail.component.css']
})
export class DomiciliosEntregaDetailComponent implements OnInit {

    domicilioEntrega: DomicilioEntrega;
    searching: boolean = false;
    searchFailed: boolean = false;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private location: Location,
        private domicilioEntregaService: DomicilioEntregaService,
        private localidadService: LocalidadService,
        private modalService: ModalService
    ) {
    }

    ngOnInit() {
        this.get();
    }

    get(): void {
        const id = this.route.snapshot.paramMap.get('id');
        if (id === 'new') {
            this.domicilioEntrega = <DomicilioEntrega>{
                id: null,
                calle: null,
                pisoDpto: null,
                localidad: <Localidad>{},
            };
        } else {
            this.domicilioEntregaService.get(+id)
                .subscribe(dataPackage => this.domicilioEntrega = <DomicilioEntrega>dataPackage.data
                );
        }
    }

    goBack(): void {
        this.location.back();
    }

    save(): void {
        this.domicilioEntregaService.save(this.domicilioEntrega)
            .subscribe(dataPackage => {
                this.domicilioEntrega = <DomicilioEntrega>dataPackage.data;
                this.goBack();
            });
    }

    searchType = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            tap(() => this.searching = true),
            switchMap(term =>
                this.domicilioEntregaService.searchTypes(term)
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

    searchLocalidad = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            tap(() => (this.searching = true)),
            switchMap((term) =>
                this.localidadService
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
