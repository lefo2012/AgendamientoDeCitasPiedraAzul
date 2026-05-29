import { DoctorDto } from './DoctorDto';
import { IntervalDto } from './IntervalDto';

export interface PendingAppointment {
  id?: number;
  doctor?: DoctorDto;
  appointmentDate?: string;
  appointmentStatus?: string;
  interval?: IntervalDto;
}
