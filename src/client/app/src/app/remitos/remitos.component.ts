import { Component, OnInit } from '@angular/core';
import { RemitoService } from '../remito.service';
import { NgbDateStruct, NgbCalendar, } from "@ng-bootstrap/ng-bootstrap";
import { ResultsPage } from "../results-page";
import { Remito } from '../remito';

@Component({
    selector: 'app-remitos',
    templateUrl: './remitos.component.html',
    styleUrls: ['./remitos.component.css']
})
export class RemitosComponent implements OnInit {

    resultsPage: ResultsPage = <ResultsPage>{};
    pages: number[];
    currentPage: number = 1;
    remito: Remito;
    date: NgbDateStruct;
    alert = true;
    mensaje = "";
    type = "";

    constructor(
        private remitoService: RemitoService,
        private calendar: NgbCalendar,
    ) { }

    ngOnInit() {
        this.getRemitos();
    }

    getRemitos(): void {
        this.date = this.calendar.getToday();
        this.remitoService.byPage(this.currentPage, 10).subscribe((dataPackage) => {
            this.resultsPage = <ResultsPage>dataPackage.data;
            this.pages = Array.from(Array(this.resultsPage.last).keys());
        });
    }

    generar(): void {
        this.remito = <Remito>{
            fechaArmado: new Date()
        }
        this.remito.fechaArmado = new Date(
            this.date.year,
            this.date.month - 1,
            this.date.day
        );
        this.remitoService.generar(this.remito.fechaArmado)
            .subscribe(dataPackage => {
                this.alert = false;
                this.mensaje = dataPackage.StatusText;
                this.type = dataPackage.StatusCode == 200 ? "succes" : "danger";
                this.remito = <Remito>dataPackage.data;
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
        this.getRemitos();
    };

}
