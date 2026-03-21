import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RescheduleAppointment } from './reschedule-appointment';

describe('RescheduleAppointment', () => {
  let component: RescheduleAppointment;
  let fixture: ComponentFixture<RescheduleAppointment>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RescheduleAppointment]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RescheduleAppointment);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
