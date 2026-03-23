import { IntervalListDto } from './IntervalListDto';

export interface ScheduleDto {
  availableTimes: Record<string, IntervalListDto>;
}
