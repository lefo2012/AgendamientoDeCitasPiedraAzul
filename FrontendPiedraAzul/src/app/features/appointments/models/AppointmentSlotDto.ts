import { IntervalDto } from './IntervalDto';

export interface AppointmentSlotDto {
  appointmentDate: string;
  interval: IntervalDto;
  label: string;
}
