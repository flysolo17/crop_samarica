import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUserGuideComponent } from './create-user-guide.component';

describe('CreateUserGuideComponent', () => {
  let component: CreateUserGuideComponent;
  let fixture: ComponentFixture<CreateUserGuideComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateUserGuideComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreateUserGuideComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
