import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { RegisterUser } from './register-user';

describe('RegisterUser', () => {
  let component: RegisterUser;
  let fixture: ComponentFixture<RegisterUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterUser],
      providers: [provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterUser);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
