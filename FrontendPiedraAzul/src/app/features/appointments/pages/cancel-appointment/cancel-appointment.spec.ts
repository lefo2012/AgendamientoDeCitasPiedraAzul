import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CancelAppointment } from './cancel-appointment';

describe('CancelAppointment', () => {
  let component: CancelAppointment;
  let fixture: ComponentFixture<CancelAppointment>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CancelAppointment]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CancelAppointment);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
