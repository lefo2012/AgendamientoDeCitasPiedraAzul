import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { CreateAppointment } from './create-appointment';
import { AppointmentService } from '../../services/appointment.service';
import { DoctorService } from '../../services/doctor.service';
import { PatientService } from '../../services/patient.service';

describe('CreateAppointment', () => {
  let component: CreateAppointment;
  let fixture: ComponentFixture<CreateAppointment>;
  const doctorServiceMock: Partial<DoctorService> = {
    getAllDoctors: () => of([])
  };
  const patientServiceMock: Partial<PatientService> = {
    getPatientByIdentificationNumber: () =>
      of({
        id: 1,
        identificationNumber: '1',
        firstName: 'Paciente',
        lastName: 'Prueba',
        phone: '3000000000',
        gender: 'M'
      })
  };
  const appointmentServiceMock: Partial<AppointmentService> = {
    reserveAppointment: () => of('ok')
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateAppointment],
      providers: [
        provideRouter([]),
        { provide: DoctorService, useValue: doctorServiceMock },
        { provide: PatientService, useValue: patientServiceMock },
        { provide: AppointmentService, useValue: appointmentServiceMock }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CreateAppointment);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
