import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateDoctor } from './update-doctor';

describe('UpdateDoctor', () => {
  let component: UpdateDoctor;
  let fixture: ComponentFixture<UpdateDoctor>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateDoctor]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateDoctor);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
