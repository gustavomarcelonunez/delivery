import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProvinciasDetailComponent } from './provincias-detail.component';

describe('ProvinciasDetailComponent', () => {
  let component: ProvinciasDetailComponent;
  let fixture: ComponentFixture<ProvinciasDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProvinciasDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProvinciasDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
