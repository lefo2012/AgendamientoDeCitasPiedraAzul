import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { CreateAppointment } from './create-appointment';

describe('CreateAppointment', () => {
  let component: CreateAppointment;
  let fixture: ComponentFixture<CreateAppointment>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateAppointment],
      providers: [provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(CreateAppointment);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
