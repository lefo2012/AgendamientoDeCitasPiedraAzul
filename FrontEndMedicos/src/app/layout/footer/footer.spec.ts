import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { Footer } from './footer';
import { AuthService } from '../../features/users/services/auth.service';
import { AUTH_CONFIG, defaultAuthConfig } from '../../core/auth/auth.config';

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

describe('Footer', () => {
  let component: Footer;
  let fixture: ComponentFixture<Footer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Footer],
      providers: [
        provideRouter([]),
        { provide: AUTH_CONFIG, useValue: defaultAuthConfig },
        { provide: AuthService, useValue: authServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Footer);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
