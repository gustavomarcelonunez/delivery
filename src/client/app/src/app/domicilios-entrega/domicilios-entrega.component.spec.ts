import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DomiciliosEntregaComponent } from './domicilios-entrega.component';

describe('DomiciliosEntregaComponent', () => {
  let component: DomiciliosEntregaComponent;
  let fixture: ComponentFixture<DomiciliosEntregaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DomiciliosEntregaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DomiciliosEntregaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
