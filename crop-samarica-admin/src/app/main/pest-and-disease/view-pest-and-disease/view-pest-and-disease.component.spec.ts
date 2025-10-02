import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewPestAndDiseaseComponent } from './view-pest-and-disease.component';

describe('ViewPestAndDiseaseComponent', () => {
  let component: ViewPestAndDiseaseComponent;
  let fixture: ComponentFixture<ViewPestAndDiseaseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewPestAndDiseaseComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ViewPestAndDiseaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
