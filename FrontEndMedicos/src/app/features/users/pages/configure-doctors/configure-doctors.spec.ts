import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigureDoctors } from './configure-doctors';

describe('ConfigureDoctors', () => {
  let component: ConfigureDoctors;
  let fixture: ComponentFixture<ConfigureDoctors>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfigureDoctors]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfigureDoctors);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
