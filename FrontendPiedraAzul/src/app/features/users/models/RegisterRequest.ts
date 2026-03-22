import { RegisterUserData } from './RegisterUserData';

export interface RegisterRequest {
  documentType: string;
  identificationNumber: string;
  firstName: string;
  lastName: string;
  birthDate: string;
  phone: string;
  active: boolean;
  user: RegisterUserData;
}