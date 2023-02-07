import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PedidosDetailComponent } from './pedidos-detail.component';

describe('PedidosDetailComponent', () => {
  let component: PedidosDetailComponent;
  let fixture: ComponentFixture<PedidosDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PedidosDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PedidosDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
