import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { Navbar } from './navbar';
import { AuthService } from '../../features/users/services/auth.service';

describe('Navbar', () => {
  let component: Navbar;
  let fixture: ComponentFixture<Navbar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Navbar],
      providers: [
        provideRouter([]),
        {
          provide: AuthService,
          useValue: {
            isAuthenticated: () => false,
            logout: () => of({}),
            clearSession: () => undefined
          }
        }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Navbar);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
