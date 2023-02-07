import { Component, OnInit } from '@angular/core';
import { FacturaService } from '../factura.service';
import { NgbDateStruct, NgbCalendar, } from "@ng-bootstrap/ng-bootstrap";;
import { ResultsPage } from "../results-page";
import { Factura } from '../factura';

@Component({
    selector: 'app-facturas',
    templateUrl: './facturas.component.html',
    styleUrls: ['./facturas.component.css']
})
export class FacturasComponent implements OnInit {

    resultsPage: ResultsPage = <ResultsPage>{};
    pages: number[];
    currentPage: number = 1;
    factura: Factura;
    date: NgbDateStruct;
    alert = true;
    mensaje = "";
    type = "";

    constructor(
        private facturaService: FacturaService,
        private calendar: NgbCalendar,
    ) { }

    ngOnInit() {
        this.getFacturas();
    }

    getFacturas(): void {
        this.date = this.calendar.getToday();
        this.facturaService.byPage(this.currentPage, 10).subscribe((dataPackage) => {
            this.resultsPage = <ResultsPage>dataPackage.data;
            this.pages = Array.from(Array(this.resultsPage.last).keys());
        });
    }

    generar(): void {
        this.factura = <Factura>{
            fechaEmision: new Date()
        }
        this.factura.fechaEmision = new Date(
            this.date.year,
            this.date.month - 1,
            this.date.day
        );
        this.facturaService.generar(this.factura.fechaEmision)
            .subscribe(dataPackage => {
                this.alert = false;
                this.mensaje = dataPackage.StatusText;
                this.type = dataPackage.StatusCode == 200 ? "succes" : "danger";
                this.factura = <Factura>dataPackage.data;
            });
    }

    showPage(pageId: number): void {
        if (!this.currentPage) {
            this.currentPage = 1;
        }
        let page = pageId;
        if (pageId == -2) { // First
            page = 1;
        }
        if (pageId == -1) { // Previous
            page = this.currentPage > 1 ? this.currentPage - 1 : this.currentPage;
        }
        if (pageId == -3) { // Next
            page = this.currentPage < this.resultsPage.last ? this.currentPage + 1 : this.currentPage;
        }
        if (pageId == -4) { // Last
            page = this.resultsPage.last;
        }
        if (pageId > 1 && this.pages.length >= pageId) { // Number
            page = this.pages[pageId - 1] + 1;
        }
        this.currentPage = page;
        this.getFacturas();
    };

}
