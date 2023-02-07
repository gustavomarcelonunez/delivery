import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, tap, switchMap, map } from 'rxjs/operators';

import { NgbDateStruct, NgbCalendar, NgbDate, } from "@ng-bootstrap/ng-bootstrap";
import { ResultsPage } from "../results-page";
import { Factura } from '../factura';
import { FacturaService } from '../factura.service';
import { ClienteService } from '../cliente.service';
import { ModalService } from "../modal.service";

@Component({
    selector: 'app-facturas-detail',
    templateUrl: './facturas-detail.component.html',
    styleUrls: ['./facturas-detail.component.css']
})
export class FacturasDetailComponent implements OnInit {

    factura: Factura;
    date: NgbDateStruct;
    searching: boolean = false;
    searchFailed: boolean = false;
    resultsPage: ResultsPage = <ResultsPage>{};
    pages: number[];
    currentPage: number = 1;

    constructor(
        private route: ActivatedRoute,
        private facturaService: FacturaService,
        private location: Location,
        private clienteService: ClienteService,
        private calendar: NgbCalendar,
    ) {
    }

    ngOnInit() {
        this.get();
    }

    get(): void {
        this.date = this.calendar.getToday();
        const id = this.route.snapshot.paramMap.get("id");

        this.facturaService.get(+id).subscribe((dataPackage) =>
            this.factura = <Factura>dataPackage.data
        );
        this.getArticulosPedido();
    }


    goBack(): void {
        this.location.back();
    }

    save(): void {
        this.factura.fechaPago = new Date(
            this.date.year,
            this.date.month - 1,
            this.date.day
        );
        this.facturaService.save(this.factura.id, this.factura.fechaPago)
            .subscribe(dataPackage => {
                this.factura = <Factura>dataPackage.data;
            });
        this.goBack();
    }

    getArticulosPedido(): void {
        const id = this.route.snapshot.paramMap.get('id');
        this.facturaService.get(+id)
            .subscribe(dataPackage => this.factura = <Factura>dataPackage.data
            );
        this.facturaService.articulosByPage(+id, this.currentPage, 10).subscribe((dataPackage) => {
            this.resultsPage = <ResultsPage>dataPackage.data;
            this.pages = Array.from(Array(this.resultsPage.last).keys());
        });
    }

    searchType = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            tap(() => this.searching = true),
            switchMap(term =>
                this.facturaService.searchTypes(term)
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

    resultFormat(value: any) {
        return value.razonSocial;
    }

    inputFormat(value: any) {
        if (value) return value.razonSocial;
        return null;
    }

}
