import { ScheduleDto } from "./ScheduleDto";

export interface DoctorDto {
    id: number;
    name: string;
    schedule: ScheduleDto;
}
