import { IntervalDto } from './IntervalDto';
import { ScheduleDto } from './ScheduleDto';

export interface DoctorDto {
  id: number;
  identificationNumber: string;
  firstName: string;
  lastName: string;
  canSchedule: boolean;
  appointmentInterval: IntervalDto | null;
  schedule: ScheduleDto | null;
}
