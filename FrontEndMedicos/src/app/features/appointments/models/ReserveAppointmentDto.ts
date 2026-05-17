import { IntervalDto } from './IntervalDto';

export interface ReserveAppointmentDto {
  idAppointment?: number;  
  idPatient: number;
  idDoctor: number;
  interval: IntervalDto;
  appointmentDate: string;
}
