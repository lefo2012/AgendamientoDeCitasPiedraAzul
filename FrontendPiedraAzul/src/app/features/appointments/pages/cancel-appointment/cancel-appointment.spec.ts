import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { CancelAppointment } from './cancel-appointment';
import { AuthService } from '../../../users/services/auth.service';
import { AUTH_CONFIG, defaultAuthConfig } from '../../../../core/auth/auth.config';

const authServiceMock = {
  isAuthenticated: () => false,
  logout: () => of({}),
  clearSession: () => undefined
};

const activatedRouteMock = {
  snapshot: { params: {}, queryParams: {} },
  params: of({}),
  queryParams: of({})
};

describe('CancelAppointment', () => {
  let component: CancelAppointment;
  let fixture: ComponentFixture<CancelAppointment>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CancelAppointment],
      providers: [
        provideRouter([]),
        { provide: AUTH_CONFIG, useValue: defaultAuthConfig },
        { provide: AuthService, useValue: authServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CancelAppointment);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
