import { RegisterRequest } from './RegisterRequest';

export interface RegisterDoctorRequest extends RegisterRequest {
  specialties: string[];
  canSchedule: boolean;
  appointmentInterval: {
    startTime: string;
    endTime: string;
  };
}