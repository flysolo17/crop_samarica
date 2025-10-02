import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewUserGuideComponent } from './view-user-guide.component';

describe('ViewUserGuideComponent', () => {
  let component: ViewUserGuideComponent;
  let fixture: ComponentFixture<ViewUserGuideComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewUserGuideComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ViewUserGuideComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
