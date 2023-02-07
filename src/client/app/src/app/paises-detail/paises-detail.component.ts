import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, tap, switchMap, map } from 'rxjs/operators';

import { Pais } from '../pais';
import { PaisService } from '../pais.service';


@Component({
  selector: 'app-paises-detail',
  templateUrl: './paises-detail.component.html',
  styleUrls: ['./paises-detail.component.css']
})
export class PaisesDetailComponent implements OnInit {

  pais: Pais;
  searching: boolean = false;
  searchFailed: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private paisService: PaisService,
    private location: Location
  ) {
  }

  ngOnInit() {
    this.get();
  }

  get(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id === 'new') {
      this.pais = <Pais>{
        id: null,
        nombre: null,
      };
    } else {
      this.paisService.get(+id)
        .subscribe(dataPackage => this.pais = <Pais>dataPackage.data
        );
    }
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    this.paisService.save(this.pais)
      .subscribe(dataPackage => {
        this.pais = <Pais>dataPackage.data;
        this.goBack();
      });
  }

  searchType = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => this.searching = true),
      switchMap(term =>
        this.paisService.searchTypes(term)
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
