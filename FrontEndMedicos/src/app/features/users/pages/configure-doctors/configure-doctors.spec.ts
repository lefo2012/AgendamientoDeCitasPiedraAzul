import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PLATFORM_ID } from '@angular/core';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { ConfigureDoctors } from './configure-doctors';
import { DoctorService } from '../../../appointments/services/doctor.service';
import { MatSnackBar } from '@angular/material/snack-bar';

const doctorServiceMock: Partial<DoctorService> = {
  getAllConfigurableDoctors: () => of([])
};

const snackBarMock = {
  open: () => undefined
};

describe('ConfigureDoctors', () => {
  let component: ConfigureDoctors;
  let fixture: ComponentFixture<ConfigureDoctors>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfigureDoctors],
      providers: [
        provideRouter([]),
        { provide: PLATFORM_ID, useValue: 'server' },
        { provide: DoctorService, useValue: doctorServiceMock },
        { provide: MatSnackBar, useValue: snackBarMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ConfigureDoctors);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
