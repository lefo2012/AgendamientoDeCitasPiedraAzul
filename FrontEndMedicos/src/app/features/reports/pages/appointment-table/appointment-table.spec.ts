import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppointmentTable } from './appointment-table';
import { AuthService } from '../../../users/services/auth.service';

const authServiceMock = {
  canScheduleAppointments: () => true
};

describe('AppointmentTable', () => {
  let component: AppointmentTable;
  let fixture: ComponentFixture<AppointmentTable>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppointmentTable],
      providers: [{ provide: AuthService, useValue: authServiceMock }]
    }).compileComponents();

    fixture = TestBed.createComponent(AppointmentTable);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
