import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DomiciliosEntregaDetailComponent } from './domicilios-entrega-detail.component';

describe('DomiciliosEntregaDetailComponent', () => {
  let component: DomiciliosEntregaDetailComponent;
  let fixture: ComponentFixture<DomiciliosEntregaDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DomiciliosEntregaDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DomiciliosEntregaDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
