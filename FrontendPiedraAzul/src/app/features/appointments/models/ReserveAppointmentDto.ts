import { IntervalDto } from './IntervalDto';

export interface ReserveAppointmentDto {
  idPatient: number;
  idDoctor: number;
  interval: IntervalDto;
  appointmentDate: string;
}
