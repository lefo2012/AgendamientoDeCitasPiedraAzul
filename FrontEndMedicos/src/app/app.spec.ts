import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { App } from './app';
import { AuthService } from './features/users/services/auth.service';
import { AUTH_CONFIG, defaultAuthConfig } from './core/auth/auth.config';

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

const activatedRouteMock = {
  snapshot: { params: {}, queryParams: {} },
  params: of({}),
  queryParams: of({})
};

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [
        provideRouter([]),
        { provide: AUTH_CONFIG, useValue: defaultAuthConfig },
        { provide: AuthService, useValue: authServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock }
      ]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should render router outlet', async () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('router-outlet')).toBeTruthy();
  });
});
