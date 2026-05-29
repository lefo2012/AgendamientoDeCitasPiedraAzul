export interface AppointmentDoctorDto {
  id: number;
  documentType: string;
  identificationNumber: string;
  firstName: string;
  lastName: string;
  birthDate: string;
  phone: string;
  specialties: string[];
}