import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppointmentButtons } from './appointment-buttons';
import { AuthService } from '../../../features/users/services/auth.service';
import { AUTH_CONFIG, defaultAuthConfig } from '../../../core/auth/auth.config';

const authServiceMock = {
  canScheduleAppointments: () => true
};

describe('AppointmentButtons', () => {
  let component: AppointmentButtons;
  let fixture: ComponentFixture<AppointmentButtons>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppointmentButtons],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: AUTH_CONFIG, useValue: defaultAuthConfig }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AppointmentButtons);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});