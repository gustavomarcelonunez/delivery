import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticulosPedidoComponent } from './articulospedido.component';

describe('ArticulospedidoComponent', () => {
  let component: ArticulosPedidoComponent;
  let fixture: ComponentFixture<ArticulosPedidoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArticulosPedidoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticulosPedidoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
