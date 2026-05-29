import { GenderEnum } from "./GenderEnum";

export interface UpdateDoctorDto {
  id: number;
  documentType: string;
  identificationNumber: string;
  firstName: string;
  lastName: string;
  birthDate: string;
  phone: string;
  gender: GenderEnum;
  active: boolean;
  specialties: string[];
  canSchedule: boolean;
  appointmentInterval: {
    startTime: string;
    endTime: string;
  };
}
