import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUpdateUserGuideComponent } from './create-update-user-guide.component';

describe('CreateUpdateUserGuideComponent', () => {
  let component: CreateUpdateUserGuideComponent;
  let fixture: ComponentFixture<CreateUpdateUserGuideComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateUpdateUserGuideComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreateUpdateUserGuideComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
