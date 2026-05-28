import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { ConfigureScheduleDoctor } from './configure-schedule-doctor';
import { DoctorService } from '../../../appointments/services/doctor.service';
import { AuthService } from '../../services/auth.service';
import { AUTH_CONFIG, defaultAuthConfig } from '../../../../core/auth/auth.config';

const authServiceMock = {
  currentPatient: () => ({ id: 1 }),
  roles: () => [],
  isAuthenticated: () => false,
  isAdmin: () => false,
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
    queryParamMap: {
      get: () => '1'
    }
  }
};

const doctorServiceMock: Partial<DoctorService> = {
  saveDoctorSchedule: () => of('ok')
};

describe('ConfigureScheduleDoctor', () => {
  let component: ConfigureScheduleDoctor;
  let fixture: ComponentFixture<ConfigureScheduleDoctor>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfigureScheduleDoctor],
      providers: [
        provideRouter([]),
        { provide: AUTH_CONFIG, useValue: defaultAuthConfig },
        { provide: AuthService, useValue: authServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: DoctorService, useValue: doctorServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ConfigureScheduleDoctor);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
