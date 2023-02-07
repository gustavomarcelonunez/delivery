import { Component, OnInit } from '@angular/core';
import { ProvinciaService } from '../provincia.service'
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { NgbdModalConfirm } from "../modal-component";
import { ResultsPage } from "../results-page";

@Component({
    selector: 'app-provincias',
    templateUrl: './provincias.component.html',
    styleUrls: ['./provincias.component.css']
})
export class ProvinciasComponent implements OnInit {

    resultsPage: ResultsPage = <ResultsPage>{};
    pages: number[];
    currentPage: number = 1;

    constructor(
        private provinciaService: ProvinciaService,
        private _modalService: NgbModal
    ) { }

    ngOnInit() {
        this.getProvincias();
    }

    getProvincias(): void {
        this.provinciaService.byPage(this.currentPage, 10).subscribe((dataPackage) => {
            this.resultsPage = <ResultsPage>dataPackage.data;
            this.pages = Array.from(Array(this.resultsPage.last).keys());
        });
    }

    remove(id: number): void {
        const modal = this._modalService.open(NgbdModalConfirm);
        const that = this;
        modal.result.then(
            function () {
                that.provinciaService.remove(id).subscribe(_ => that.getProvincias());
            },
            function () { }
        );
        modal.componentInstance.title = "Eliminar provincia";
        modal.componentInstance.message = "¿Esta seguro de eliminar la provincia?";
        modal.componentInstance.description =
            "Si elimina la provincia no la podrá utilizar luego.";
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
        this.getProvincias();
    };


}
