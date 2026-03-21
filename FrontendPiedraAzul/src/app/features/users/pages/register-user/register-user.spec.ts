import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { RegisterUser } from './register-user';
import { AUTH_CONFIG, defaultAuthConfig } from '../../services/auth.config';

describe('RegisterUser', () => {
  let component: RegisterUser;
  let fixture: ComponentFixture<RegisterUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterUser, HttpClientTestingModule],
      providers: [{ provide: AUTH_CONFIG, useValue: defaultAuthConfig }]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterUser);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
