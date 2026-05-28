import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { RegisterUser } from './register-user';
import { AuthService } from '../../services/auth.service';
import { AUTH_CONFIG, defaultAuthConfig } from '../../../../core/auth/auth.config';

const authServiceMock = {
  currentPatient: () => null,
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

describe('RegisterUser', () => {
  let component: RegisterUser;
  let fixture: ComponentFixture<RegisterUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterUser],
      providers: [
        provideRouter([]),
        { provide: AUTH_CONFIG, useValue: defaultAuthConfig },
        { provide: AuthService, useValue: authServiceMock }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterUser);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
