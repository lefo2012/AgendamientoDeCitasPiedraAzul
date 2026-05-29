export interface PatientProfile {
  id?: number | string;
  documentType?: string;
  identificationNumber?: string;
  firstName?: string;
  lastName?: string;
  birthDate?: string;
  phone?: string;
  active?: boolean;
  gender?: string;
  user?: {
    id?: number | string;
    email?: string;
  };
}
