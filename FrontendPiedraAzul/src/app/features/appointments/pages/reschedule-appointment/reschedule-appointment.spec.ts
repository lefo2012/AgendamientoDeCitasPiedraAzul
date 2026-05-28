import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { PLATFORM_ID } from '@angular/core';
import { of } from 'rxjs';
import { RescheduleAppointment } from './reschedule-appointment';
import { AppointmentService } from '../../services/appointment.service';
import { DoctorService } from '../../services/doctor.service';
import { AuthService } from '../../../users/services/auth.service';
import { AUTH_CONFIG, defaultAuthConfig } from '../../../../core/auth/auth.config';

const authServiceMock = {
	currentPatient: () => null,
	roles: () => [],
	isAuthenticated: () => false,
	register: () => of({}),
	registerDoctor: () => of({}),
	login: () => of(void 0),
	logout: () => of(void 0),
	initializeSession: () => of(null),
	restoreSession: () => of(null),
	refreshAccessToken: () => of(void 0),
	clearSession: () => undefined,
	getRoles: () => []
};

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
	getDoctorById: () =>
		of({
			id: 1,
			documentType: 'CC',
			identificationNumber: '123',
			firstName: 'Ana',
			lastName: 'Perez',
			birthDate: '1990-01-01',
			phone: '3000000000',
			specialties: ['General']
		}),
	getDoctorsBySpeciality: () =>
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
	getPendingAppointments: () =>
		of([
			{
				id: 1,
				doctor: {
					id: 1,
					identificationNumber: '123',
					firstName: 'Ana',
					lastName: 'Perez',
					canSchedule: true,
					appointmentInterval: { startTime: '08:00', endTime: '08:30' },
					schedule: { availableTimes: {} }
				}
			}
		]),
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
				{ provide: AUTH_CONFIG, useValue: defaultAuthConfig },
				{ provide: AuthService, useValue: authServiceMock },
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
