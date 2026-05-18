import { RegisterUserData } from './RegisterUserData';
import { GenderEnum } from './GenderEnum';

export interface RegisterRequest {
  id?: number; // opcional para creación, requerido para actualización
  documentType: string;
  identificationNumber: string;
  firstName: string;
  lastName: string;
  birthDate: string;
  phone: string;
  gender: GenderEnum;
  active: boolean;
  user: RegisterUserData;
}