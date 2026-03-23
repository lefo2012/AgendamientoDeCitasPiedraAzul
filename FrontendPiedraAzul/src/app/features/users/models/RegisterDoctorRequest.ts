import { RegisterRequest } from './RegisterRequest';

export interface RegisterDoctorRequest extends RegisterRequest {
  specialties: string[];
}