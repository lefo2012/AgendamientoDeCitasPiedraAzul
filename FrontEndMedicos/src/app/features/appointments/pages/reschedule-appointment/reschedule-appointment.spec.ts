import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { PLATFORM_ID } from '@angular/core';
import { of } from 'rxjs';
import { RescheduleAppointment } from './reschedule-appointment';
import { DoctorService } from '../../services/doctor.service';
import { AppointmentService } from '../../services/appointment.service';

const activatedRouteMock = {
  snapshot: {
    paramMap: {
      get: () => '1'
    },
    queryParamMap: {
      get: () => '1'
    }
  }
};

const doctorServiceMock: Partial<DoctorService> = {
  getAllDoctors: () =>
    of([
      {
        id: 1,
        identificationNumber: '123',
        firstName: 'Ana',
        lastName: 'Perez',
        canSchedule: true,
        appointmentInterval: { startTime: '08:00', endTime: '08:30' },
        schedule: { availableTimes: {} }
      }
    ])
};

const appointmentServiceMock: Partial<AppointmentService> = {
  rescheduleAppointment: () => of('ok')
};

describe('RescheduleAppointment', () => {
  let component: RescheduleAppointment;
  let fixture: ComponentFixture<RescheduleAppointment>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RescheduleAppointment],
      providers: [
        provideRouter([]),
        { provide: PLATFORM_ID, useValue: 'browser' },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: DoctorService, useValue: doctorServiceMock },
        { provide: AppointmentService, useValue: appointmentServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RescheduleAppointment);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
