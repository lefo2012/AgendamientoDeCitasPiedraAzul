export interface AppointmentReportDto {
  id?: number;
  patientId: number;
  patientName: string;
  doctorName: string;
  appointmentInterval: string;
  date: string;
}
