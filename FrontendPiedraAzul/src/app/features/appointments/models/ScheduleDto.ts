import { IntervalListDto } from "./IntervalListDto";

export interface ScheduleDto{
    availableTimes: Map<string, IntervalListDto>
}