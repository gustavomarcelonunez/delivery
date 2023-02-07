import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LocalidadesDetailComponent } from './localidades-detail.component';

describe('LocalidadesDetailComponent', () => {
  let component: LocalidadesDetailComponent;
  let fixture: ComponentFixture<LocalidadesDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LocalidadesDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LocalidadesDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
