import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, tap, switchMap, map } from 'rxjs/operators';

import { Remito } from '../remito';
import { RemitoService } from '../remito.service';
import { ResultsPage } from "../results-page";


@Component({
    selector: 'app-remitos-detail',
    templateUrl: './remitos-detail.component.html',
    styleUrls: ['./remitos-detail.component.css']
})
export class RemitosDetailComponent implements OnInit {

    remito: Remito;
    searching: boolean = false;
    searchFailed: boolean = false;
    resultsPage: ResultsPage = <ResultsPage>{};
    pages: number[];
    currentPage: number = 1;

    constructor(
        private route: ActivatedRoute,
        private remitoService: RemitoService,
        private location: Location,
    ) {
    }

    ngOnInit() {
        this.get();
    }

    get(): void {
        const id = this.route.snapshot.paramMap.get("id");

        this.remitoService.get(+id)
            .subscribe(dataPackage => this.remito = <Remito>dataPackage.data
            );
        this.getArticulosRemito();

    }

    goBack(): void {
        this.location.back();
    }

    save(): void {
        this.remitoService.save(this.remito)
            .subscribe(dataPackage => {
                this.remito = <Remito>dataPackage.data;
                this.goBack();
            });

    }

    getArticulosRemito(): void {
        const id = this.route.snapshot.paramMap.get('id');
        this.remitoService.get(+id)
            .subscribe(dataPackage => this.remito = <Remito>dataPackage.data
            );
        this.remitoService.articulosByPage(+id, this.currentPage, 10).subscribe((dataPackage) => {
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
                this.remitoService.searchTypes(term)
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

}
