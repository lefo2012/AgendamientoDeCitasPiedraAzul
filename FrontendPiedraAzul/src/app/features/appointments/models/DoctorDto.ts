import { ScheduleDTO } from './ScheduleDto';

export interface DoctorDTO {
  id: number;
  name: string;
  schedule: ScheduleDTO;
}
