import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { RegisterDoctor } from './register-doctor';
import { AUTH_CONFIG, defaultAuthConfig } from '../../services/auth.config';

describe('RegisterDoctor', () => {
  let component: RegisterDoctor;
  let fixture: ComponentFixture<RegisterDoctor>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterDoctor, HttpClientTestingModule],
      providers: [{ provide: AUTH_CONFIG, useValue: defaultAuthConfig }]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterDoctor);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
