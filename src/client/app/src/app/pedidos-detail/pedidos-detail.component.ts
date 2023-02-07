import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, tap, switchMap, map } from 'rxjs/operators';

import { NgbDateStruct, NgbCalendar, NgbDate, } from "@ng-bootstrap/ng-bootstrap";

import { Pedido } from '../pedido';
import { PedidoService } from '../pedido.service';
import { ClienteService } from '../cliente.service';
import { ArticuloService } from '../articulo.service';
import { ArticulosPedido } from '../articulos-pedido';
import { DomicilioEntregaService } from '../domicilio-entrega.service';
import { LocalidadService } from '../localidad.service';
import { ModalService } from "../modal.service";

@Component({
    selector: 'app-pedidos-detail',
    templateUrl: './pedidos-detail.component.html',
    styleUrls: ['./pedidos-detail.component.css']
})
export class PedidosDetailComponent implements OnInit {

    pedido: Pedido;
    date: NgbDateStruct;
    searching: boolean = false;
    searchFailed: boolean = false;

    constructor(
        private route: ActivatedRoute,
        private pedidoService: PedidoService,
        private clienteService: ClienteService,
        private localidadService: LocalidadService,
        private articuloService: ArticuloService,
        private domicilioEntregaService: DomicilioEntregaService,
        private calendar: NgbCalendar,
        private location: Location,
        private modalService: ModalService
    ) {
    }

    ngOnInit() {
        this.get();
    }

    get(): void {
        const id = this.route.snapshot.paramMap.get("id");
        if (id === 'new') {
            this.pedido = <Pedido>{
                date: new Date(),
                articulosPedido: <ArticulosPedido[]>[],
            };
            this.date = this.calendar.getToday();
        } else {
            this.pedidoService.get(+id).subscribe((dataPackage) => {
                this.pedido = <Pedido>dataPackage.data;
                this.pedido.date = new Date(this.pedido.date);
                this.date = NgbDate.from({
                    day: this.pedido.date.getUTCDate(),
                    month: this.pedido.date.getUTCMonth() + 1,
                    year: this.pedido.date.getUTCFullYear(),
                });
            });
        }
    }

    goBack(): void {
        this.location.back();
    }

    save(): void {
        this.pedido.date = new Date(
            this.date.year,
            this.date.month - 1,
            this.date.day
        );
        this.pedidoService.save(this.pedido)
            .subscribe(dataPackage => {
                this.pedido = <Pedido>dataPackage.data;
                this.goBack()
            });
    }

    searchType = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            tap(() => this.searching = true),
            switchMap(term =>
                this.pedidoService.searchTypes(term)
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

    searchCliente = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            tap(() => (this.searching = true)),
            switchMap((term) =>
                this.clienteService
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

    addArticulosPedido(): void {
        this.pedido.articulosPedido.push({
            id: null, cantidad: null, precioUnitario: null, articulo: null,
            precio: null, pedido: this.pedido, factura: null,
            remito: null
        });
    }

    removeArticuloPedido(articuloPedido: ArticulosPedido): void {
        this.modalService
            .confirm(
                "Eliminar artículo pedido",
                "¿Está seguro de borrar este artículo pedido?",
                "El cambio no se confirmará hasta que no guarde el pedido."
            )
            .then(
                (_) => {
                    let articulosPedido = this.pedido.articulosPedido;
                    articulosPedido.splice(articulosPedido.indexOf(articuloPedido), 1);
                },
                (_) => { }
            );
    }

    searchDomicilio = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            tap(() => (this.searching = true)),
            switchMap((term) =>
                this.domicilioEntregaService
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

    searchArticulo = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            tap(() => (this.searching = true)),
            switchMap((term) =>
                this.articuloService
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
        return value.razonSocial;
    }

    inputFormat(value: any) {
        if (value) return value.razonSocial;
        return null;
    }

    resultCuitFormat(value: any) {
        return value.cuit;
    }

    inputCuitFormat(value: any) {
        if (value) return value.cuit;
        return null;
    }

    resultDomicilioFormat(value: any) {
        return value.calle;
    }

    inputDomicilioFormat(value: any) {
        if (value) return value.calle;
        return null;
    }

    resultNombreFormat(value: any) {
        return value.nombre;
    }

    inputNombreFormat(value: any) {
        if (value) return value.nombre;
        return null;
    }

    resultArticuloFormat(value: any) {
        return value.codigo;
    }

    inputArticuloFormat(value: any) {
        if (value) return value.codigo;
        return null;
    }

    resultPrecioUnitarioFormat(value: any) {
        return value.precio;
    }

    inputPrecioUnitarioFormat(value: any) {
        if (value) return value.precio;
        return null;
    }

    resultDomicilioAlturaFormat(value: any) {
        return value.altura;
    }

    inputDomicilioAlturaFormat(value: any) {
        if (value) return value.altura;
        return null;
    }

    resultDomicilioPisoFormat(value: any) {
        return value.pisoDpto;
    }

    inputDomicilioPisoFormat(value: any) {
        if (value) return value.pisoDpto;
        return null;
    }

}
