import { DayOfWeekDto } from './DayOfWeekDto';
import { IntervalListDto } from './IntervalListDto';

export interface DoctorSchedule {
  days: DayOfWeekDto[];
  schedules: IntervalListDto[];
  weeksRepeat: number;
  year: number;
}
