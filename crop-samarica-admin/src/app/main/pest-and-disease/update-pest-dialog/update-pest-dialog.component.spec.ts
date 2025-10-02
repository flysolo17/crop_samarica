import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdatePestDialogComponent } from './update-pest-dialog.component';

describe('UpdatePestDialogComponent', () => {
  let component: UpdatePestDialogComponent;
  let fixture: ComponentFixture<UpdatePestDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdatePestDialogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UpdatePestDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
