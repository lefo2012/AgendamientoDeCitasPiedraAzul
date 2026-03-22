import { DayOfWeekDto } from './DayOfWeekDto';
import { IntervalListDTO } from './IntervalListDTO';

export interface DoctorSchedule {
  days: DayOfWeekDto[];
  schedules: IntervalListDTO[];
  weeksRepeat: number;
  year: number;
}