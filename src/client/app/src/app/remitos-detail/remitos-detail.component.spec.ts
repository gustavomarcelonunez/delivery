import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RemitosDetailComponent } from './remitos-detail.component';

describe('RemitosDetailComponent', () => {
  let component: RemitosDetailComponent;
  let fixture: ComponentFixture<RemitosDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RemitosDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RemitosDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
