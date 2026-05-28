import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { Main } from './main';
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

describe('Main', () => {
  let component: Main;
  let fixture: ComponentFixture<Main>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Main],
      providers: [
        provideRouter([]),
        { provide: AUTH_CONFIG, useValue: defaultAuthConfig },
        { provide: AuthService, useValue: authServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Main);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
