import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Cliente } from '../cliente';
import { ClienteService } from '../cliente.service';
import { ArticulosPedidoService } from '../articulos-pedido.service';
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { NgbdModalConfirm } from "../modal-component";
import { ResultsPage } from "../results-page";
import { Location } from '@angular/common';

@Component({
    selector: 'app-articulospedido',
    templateUrl: './articulospedido.component.html',
    styleUrls: ['./articulospedido.component.css']
})
export class ArticulosPedidoComponent implements OnInit {

    cliente: Cliente;
    searching: boolean = false;
    searchFailed: boolean = false;
    resultsPage: ResultsPage = <ResultsPage>{};
    pages: number[];
    currentPage: number = 1;

    constructor(
        private route: ActivatedRoute,
        private articulosPedidoService: ArticulosPedidoService,
        private clienteService: ClienteService,
        private location: Location,
        private _modalService: NgbModal
    ) { }

    ngOnInit() {
        this.getArticulosPedido();
    }

    goBack(): void {
        this.location.back();
    }

    getArticulosPedido(): void {
        const cuit = this.route.snapshot.paramMap.get('cuit');
        this.clienteService.get(cuit)
            .subscribe(dataPackage => this.cliente = <Cliente>dataPackage.data
            );
        this.articulosPedidoService.byPage(cuit, this.currentPage, 10).subscribe((dataPackage) => {
            this.resultsPage = <ResultsPage>dataPackage.data;
            this.pages = Array.from(Array(this.resultsPage.last).keys());
        });
    }

    remove(id: number): void {
        const modal = this._modalService.open(NgbdModalConfirm);
        const that = this;
        modal.result.then(
            function () {
                that.articulosPedidoService.remove(id).subscribe(_ => that.getArticulosPedido());
            },
            function () { }
        );
        modal.componentInstance.title = "Eliminar artículosPedido";
        modal.componentInstance.message = "¿Esta seguro de eliminar el artículosPedido?";
        modal.componentInstance.description =
            "Si elimina el artículosPedido no la podrá utilizar luego.";
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
        this.getArticulosPedido();
    };


}
