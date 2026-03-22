import { Component, Optional } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButton } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import {MatProgressBarModule} from '@angular/material/progress-bar';


@Component({
  selector: 'app-create-appointment',
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    MatDatepickerModule,
    MatDialogModule,
    MatButton,
    MatTooltipModule,
    MatProgressBarModule

],
  templateUrl: './create-appointment.html',
  styleUrl: './create-appointment.scss',
})
export class CreateAppointment{
  appointmentForm: any;
  
  specialitySelected: any | null = null;
  doctorSelected: any | null = null;
  dateSelected: any | null = null;
  intervalSelected: any | null = null;

  hoveredSpeciality: any | null = null;
  step = 0;
  maxSteps = 5;
  doctors: any[] = [];
  intervals: any[] = [];
  dates: any[] = [];

  specialties : any = [
    { value: 'FISIOTERAPIA', viewValue: 'Fisioterapia', description: 'La fisioterapia sirve para rehabilitar, prevenir y tratar lesiones físicas, dolor crónico y problemas de movilidad' },
    { value: 'TERAPIA_NEURAL', viewValue: 'Terapia Neural', description: 'La terapia neural busca aliviar el dolor crónico y enfermedades funcionales' },
    { value: 'QUIROPRAXIA', viewValue: 'Quiropraxia' , description: 'La quiropraxia es una especialidad que se enfoca en la rehabilitación de lesiones y enfermedades musculoesqueleticas' },
  ]


  /**
   * Submit the current step and go to the next one.
   * If the current step is 0 and the specialty is not selected, it will stay in the same step.
   */
  next() {
    
    if(this.step === 0 && this.specialitySelected === null){
      this.step = 0;
    }else if(this.step === 1 && this.doctorSelected === null){
      
      this.step = 1;
    }else if(this.step === 2 && this.dateSelected === null){
      
      this.step = 2;
    }else if(this.step === 3 && this.intervalSelected === null){
      
      this.step = 3;
    }else{
      if(this.step < this.maxSteps){
          this.step++;
      }
    }
  }

  previous() {
    if(this.step > 0){
      this.step--;
    }
  }

  confirmAppointment() {
    this.dialogRef?.close();
  }

  onBack() {
    if(this.step >= 1){
      this.step--;
    }else{
      this.step = 0;
    }
    
  }
  panelX = 0;
  panelY = 0;

  onSpecialityChange() {
  if (this.specialitySelected?.value === 'FISIOTERAPIA') {
    this.doctors = [
      { value: 1, viewValue: 'Dra. Luisa' },
      { value: 2, viewValue: 'Dra. Angela' },
      { value: 3, viewValue: 'Dr. Brown Mauricio' }
    ];

    this.intervals = [
      { value: { '8:00': '8:15' }, viewValue: '8:00 - 8:15' },
      { value: { '8:15': '8:30' }, viewValue: '8:15 - 8:30' },
      { value: { '8:30': '8:45' }, viewValue: '8:30 - 8:45' },
      { value: { '8:45': '9:00' }, viewValue: '8:45 - 9:00' },
    ];
  } else {
    this.doctors = [
      { value: 1, viewValue: 'Dr. Fierro' },
      { value: 2, viewValue: 'Dr. Paco Rabana' },
      { value: 3, viewValue: 'Dr. Nelson' }
    ];

    this.intervals = [
      { value: { '8:00': '8:30' }, viewValue: '8:00 - 8:30' },
      { value: { '8:30': '9:00' }, viewValue: '8:30 - 9:00' },
      { value: { '9:00': '9:30' }, viewValue: '9:00 - 9:30' },
      { value: { '9:30': '10:00' }, viewValue: '9:30 - 10:00' },
    ];
    }
    
  }
  onDoctorChange(){

    
    
    this.dates = [
      {value: '10/05/2023', viewValue: '10/05/2023'},
      {value: '10/06/2023', viewValue: '10/06/2023'},
      {value: '10/07/2023', viewValue: '10/07/2023'}, 
    ]

  }
  

  constructor(
    private fb: FormBuilder,
    @Optional() private dialogRef: MatDialogRef<CreateAppointment> | null
  ) {}
  onHover(event: MouseEvent | null, speciality: any | null) {
    this.hoveredSpeciality = speciality;

    if (event && event.target) {
      const rect = (event.target as HTMLElement).getBoundingClientRect();

      this.panelX = rect.right + 10; 
      this.panelY = rect.top;        
    }
  }

  get progress(): number {
    return (this.step / this.maxSteps) * 100;
  }

  onCancel() {
    this.dialogRef?.close();
  }
}
