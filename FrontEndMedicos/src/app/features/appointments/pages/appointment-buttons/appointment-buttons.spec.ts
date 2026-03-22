import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppointmentButtons } from './appointment-buttons';

describe('AppointmentButtons', () => {
  let component: AppointmentButtons;
  let fixture: ComponentFixture<AppointmentButtons>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppointmentButtons]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AppointmentButtons);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
