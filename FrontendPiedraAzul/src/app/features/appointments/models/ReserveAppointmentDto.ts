import { IntervalDto } from './IntervalDto';

export interface ReserveAppointmentDto {
  id?: number;
  idPatient: number;
  idDoctor: number;
  interval: IntervalDto;
  appointmentDate: string;
}
