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
import { AppointmentService } from '../../services/appointmentService';
import { DoctorDto } from '../../models/DoctorDto';
import { IntervalDto } from '../../models/IntervalDto';

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
  doctors: DoctorDto[] = [];
  intervals: any[] = [];
  dates: any[] = [];


  constructor(private readonly appointmentService:AppointmentService,  
    private fb: FormBuilder,
    @Optional() private dialogRef: MatDialogRef<CreateAppointment> | null
  ){
  }
  

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
    this.appointmentService.getDoctorsBySpeciality(this.specialitySelected).subscribe((doctors) => {
      this.doctors = doctors;
    })  
    
  }
  onDoctorChange(){
    this.dates= Object.keys(this.doctorSelected.schedule) || [];
  }

  onDateChange(){
    
    const schedule = this.doctorSelected.schedule[this.dateSelected] || [];

    const isFisio = this.specialitySelected === 'FISIOTERAPIA';
    const minutes = isFisio ? 15 : 30;

    const result: IntervalDto[] = [];

    schedule.forEach((block : IntervalDto) => {

      let start = this.toMinutes(block.startTime);
      const end = this.toMinutes(block.endTime);

      while (start + minutes <= end) {
        result.push({
          startTime: this.toTime(start),
          endTime: this.toTime(start + minutes)
        });
        start += minutes;
      }
    });

    this.intervals = result;
  }
  
  toMinutes(time: string): number {
    const [h, m] = time.split(':').map(Number);
    return h * 60 + m;
  }

  toTime(minutes: number): string {
    const h = Math.floor(minutes / 60);
    const m = minutes % 60;
    return `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}`;
  }

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
