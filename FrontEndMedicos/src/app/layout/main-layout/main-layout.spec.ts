import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { MainLayout } from './main-layout';
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

describe('MainLayout', () => {
  let component: MainLayout;
  let fixture: ComponentFixture<MainLayout>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MainLayout],
      providers: [
        provideRouter([]),
        { provide: AUTH_CONFIG, useValue: defaultAuthConfig },
        { provide: AuthService, useValue: authServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MainLayout);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
