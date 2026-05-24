export interface UpdatePatientRequest {
  id: number | string;
  documentType: string;
  identificationNumber: string;
  firstName: string;
  lastName: string;
  birthDate: string;
  phone: string;
  active: boolean;
  gender: string;
}
