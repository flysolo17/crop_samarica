import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PestAndDiseaseComponent } from './pest-and-disease.component';

describe('PestAndDiseaseComponent', () => {
  let component: PestAndDiseaseComponent;
  let fixture: ComponentFixture<PestAndDiseaseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PestAndDiseaseComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PestAndDiseaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
