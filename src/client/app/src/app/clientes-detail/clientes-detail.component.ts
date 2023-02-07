import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, tap, switchMap, map } from 'rxjs/operators';

import { Cliente } from '../cliente';
import { ClienteService } from '../cliente.service';
import { DomicilioEntrega } from '../domicilio-entrega';
import { LocalidadService } from '../localidad.service';
import { ModalService } from "../modal.service";

@Component({
    selector: 'app-clientes-detail',
    templateUrl: './clientes-detail.component.html',
    styleUrls: ['./clientes-detail.component.css']
})
export class ClientesDetailComponent implements OnInit {

    cliente: Cliente;
    searching: boolean = false;
    searchFailed: boolean = false;

    constructor(
        private route: ActivatedRoute,
        private clienteService: ClienteService,
        private location: Location,
        private localidadService: LocalidadService,
        private modalService: ModalService
    ) {
    }

    ngOnInit() {
        this.get();
    }

    get(): void {
        const cuit = this.route.snapshot.paramMap.get('cuit');
        if (cuit === 'new') {
            this.cliente = <Cliente>{
                id: null,
                cuit: null,
                razonSocial: null,
                domiciliosEntrega: <DomicilioEntrega[]>[],
            };
        } else {
            this.clienteService.get(cuit)
                .subscribe(dataPackage => this.cliente = <Cliente>dataPackage.data
                );
        }
    }

    goBack(): void {
        this.location.back();
    }

    save(): void {
        this.clienteService.save(this.cliente)
            .subscribe(dataPackage => {
                this.cliente = <Cliente>dataPackage.data;
                this.goBack();
            });

    }

    searchType = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            tap(() => this.searching = true),
            switchMap(term =>
                this.clienteService.searchTypes(term)
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

    addDomicilio(): void {
        this.cliente.domiciliosEntrega.push({
            id: null, calle: null, altura: 0, pisoDpto: null,
            localidad: { id: null, nombre: null, cp: null, provincia: { id: null, nombre: null, pais: { id: null, nombre: null } } }
        });
    }

    removeDomicilio(domicilio: DomicilioEntrega): void {
        this.modalService
            .confirm(
                "Eliminar domicilio",
                "¿Está seguro de borrar este domicilio?",
                "El cambio no se confirmará hasta que no guarde el cliente."
            )
            .then(
                (_) => {
                    let domicilios = this.cliente.domiciliosEntrega;
                    domicilios.splice(domicilios.indexOf(domicilio), 1);
                },
                (_) => { }
            );
    }

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

        resultNameFormat(value: any) {
            return value.razonSocial;
        }
    
        inputNameFormat(value: any) {
            if (value) return value.razonSocial;
            return null;
        }

}
