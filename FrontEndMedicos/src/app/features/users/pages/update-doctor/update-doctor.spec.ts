import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { PLATFORM_ID } from '@angular/core';
import { of } from 'rxjs';
import { UpdateDoctor } from './update-doctor';
import { DoctorService } from '../../../appointments/services/doctor.service';
import { MatSnackBar } from '@angular/material/snack-bar';

const activatedRouteMock = {
  snapshot: {
    paramMap: {
      get: () => '1'
    }
  }
};

const doctorServiceMock: Partial<DoctorService> = {
  getDoctorById: () =>
    of({
      id: 1,
      documentType: 'CC',
      identificationNumber: '123',
      firstName: 'Ana',
      lastName: 'Perez',
      birthDate: '1990-01-01',
      phone: '3000000000',
      gender: 'Femenino',
      specialties: ['FISIOTERAPIA'],
      canSchedule: true,
      active: true,
      appointmentInterval: { startTime: '08:00', endTime: '08:30' }
    })
};

const snackBarMock = {
  open: () => undefined
};

describe('UpdateDoctor', () => {
  let component: UpdateDoctor;
  let fixture: ComponentFixture<UpdateDoctor>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateDoctor],
      providers: [
        provideRouter([]),
        { provide: PLATFORM_ID, useValue: 'server' },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: DoctorService, useValue: doctorServiceMock },
        { provide: MatSnackBar, useValue: snackBarMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(UpdateDoctor);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
