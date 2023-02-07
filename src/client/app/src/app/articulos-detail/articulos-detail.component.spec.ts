import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticulosDetailComponent } from './articulos-detail.component';

describe('ArticulosDetailComponent', () => {
  let component: ArticulosDetailComponent;
  let fixture: ComponentFixture<ArticulosDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArticulosDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticulosDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
