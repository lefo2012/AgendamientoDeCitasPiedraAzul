import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { CreateAppointment } from './create-appointment';
import { ScheduleService } from '../../services/schedule.service';

describe('CreateAppointment', () => {
  let component: CreateAppointment;
  let fixture: ComponentFixture<CreateAppointment>;
  const scheduleServiceMock: Partial<ScheduleService> = {
    getAllDoctors: () => of([]),
    getPatientByIdentificationNumber: () =>
      of({
        id: 1,
        identificationNumber: '1',
        firstName: 'Paciente',
        lastName: 'Prueba',
        phone: '3000000000',
        gender: 'M',
      }),
    reserveAppointment: () => of('ok'),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateAppointment],
      providers: [
        provideRouter([]),
        { provide: ScheduleService, useValue: scheduleServiceMock },
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
