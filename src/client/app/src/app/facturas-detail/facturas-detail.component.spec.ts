import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FacturasDetailComponent } from './facturas-detail.component';

describe('FacturasDetailComponent', () => {
  let component: FacturasDetailComponent;
  let fixture: ComponentFixture<FacturasDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FacturasDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FacturasDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
