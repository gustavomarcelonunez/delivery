import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, tap, switchMap, map } from 'rxjs/operators';

import { Articulo } from '../articulo';
import { ArticuloService } from '../articulo.service';

@Component({
  selector: 'app-articulos-detail',
  templateUrl: './articulos-detail.component.html',
  styleUrls: ['./articulos-detail.component.css']
})
export class ArticulosDetailComponent implements OnInit {

  articulo: Articulo;
  searching: boolean = false;
  searchFailed: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private articuloService: ArticuloService,
    private location: Location
  ) {
  }

  ngOnInit() {
    this.get();
  }

  get(): void {
    const codigo = this.route.snapshot.paramMap.get('codigo');
    if (codigo === 'new') {
      this.articulo = <Articulo>{};
    } else {
      this.articuloService.get(codigo)
        .subscribe(dataPackage => this.articulo = <Articulo>dataPackage.data
        );
    }
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    this.articuloService.save(this.articulo)
      .subscribe(dataPackage => {
        this.articulo = <Articulo>dataPackage.data;
        this.goBack();
      });
  }

  searchType = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => this.searching = true),
      switchMap(term =>
        this.articuloService.searchTypes(term)
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
