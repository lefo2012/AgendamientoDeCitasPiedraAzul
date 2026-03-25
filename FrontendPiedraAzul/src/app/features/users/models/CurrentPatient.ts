export interface CurrentPatient {
  id?: string | number;
  email?: string;
  firstName?: string;
  lastName?: string;
  [key: string]: unknown;
}