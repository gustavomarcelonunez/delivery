import { Component, OnInit } from '@angular/core';
import { ArticuloService } from '../articulo.service';
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { NgbdModalConfirm } from "../modal-component";
import { ResultsPage } from "../results-page";

@Component({
    selector: 'app-articulos',
    templateUrl: './articulos.component.html',
    styleUrls: ['./articulos.component.css']
})
export class ArticulosComponent implements OnInit {

    resultsPage: ResultsPage = <ResultsPage>{};
    pages: number[];
    currentPage: number = 1;
    alert = true;
    mensaje = "";
    type = "";

    constructor(
        private articuloService: ArticuloService,
        private _modalService: NgbModal
    ) { }

    ngOnInit() {
        this.getArticulos();
    }

    getArticulos(): void {
        this.articuloService.byPage(this.currentPage, 10).subscribe((dataPackage) => {
            this.resultsPage = <ResultsPage>dataPackage.data;
            this.pages = Array.from(Array(this.resultsPage.last).keys());
        });
    }

    remove(id: number): void {
        const modal = this._modalService.open(NgbdModalConfirm);
        const that = this;
        modal.result.then(
            function () {
                that.articuloService.remove(id).subscribe(_ => {
                    that.alert = false;
                    that.mensaje = _.StatusText;
                    that.type = _.StatusCode == 200 ? "succes" : "danger";
                    that.getArticulos()
                });
            },
            function () { }
        );
        modal.componentInstance.title = "Eliminar artículo";
        modal.componentInstance.message = "¿Esta seguro de eliminar el artículo?";
        modal.componentInstance.description =
            "Si elimina el artículo no la podrá utilizar luego.";
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
        this.getArticulos();
    };


}
