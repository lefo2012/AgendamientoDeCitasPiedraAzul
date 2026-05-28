import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { CreateAppointment } from './create-appointment';
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

describe('CreateAppointment', () => {
  let component: CreateAppointment;
  let fixture: ComponentFixture<CreateAppointment>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateAppointment],
      providers: [
        provideRouter([]),
        { provide: AUTH_CONFIG, useValue: defaultAuthConfig },
        { provide: AuthService, useValue: authServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CreateAppointment);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
