import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigureScheduleDoctor } from './configure-schedule-doctor';

describe('ConfigureScheduleDoctor', () => {
  let component: ConfigureScheduleDoctor;
  let fixture: ComponentFixture<ConfigureScheduleDoctor>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfigureScheduleDoctor]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfigureScheduleDoctor);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
