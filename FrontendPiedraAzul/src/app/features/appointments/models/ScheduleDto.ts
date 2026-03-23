import { IntervalListDTO } from './IntervalListDto';

export interface ScheduleDTO {
  availableTimes: Map<string, IntervalListDTO>;
}
