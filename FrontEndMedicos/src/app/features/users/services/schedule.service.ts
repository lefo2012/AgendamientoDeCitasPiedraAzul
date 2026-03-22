import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DoctorSchedule } from '../models/DoctorSchedule';
import { ScheduleSlot } from '../models/ScheduleSlot';

@Injectable({
  providedIn: 'root'
})
export class ScheduleService {
  private apiUrl = '/api/doctor'; // Ajusta según tu backend

  constructor(private http: HttpClient) {}

  /**
   * Genera los slots de tiempo disponibles basados en los parámetros
   */
  generateScheduleSlots(
    daysOfWeek: number[],
    startHour: number,
    endHour: number,
    intervalMinutes: number,
    weekStart: Date,
    weekEnd: Date,
    startMinute: number = 0,
    endMinute: number = 0
  ): ScheduleSlot[] {
    const slots: ScheduleSlot[] = [];
    
    // Iterar por cada día en el rango de semanas
    let currentDate = new Date(weekStart);
    while (currentDate <= weekEnd) {
      const dayOfWeek = currentDate.getDay();
      
      // Si el día de la semana está en la lista de días permitidos
      if (daysOfWeek.includes(dayOfWeek)) {
        // Generar slots para este día
        let hours = startHour;
        let minutes = startMinute;

        while (hours < endHour || (hours === endHour && minutes < endMinute)) {
          const timeString = `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`;
          
          slots.push({
            time: timeString,
            dayOfWeek: dayOfWeek,
            date: new Date(currentDate)
          });

          // Incrementar el intervalo
          minutes += intervalMinutes;
          if (minutes >= 60) {
            hours += Math.floor(minutes / 60);
            minutes = minutes % 60;
          }

          // Si hemos pasado la hora de cierre, salir del loop
          if (hours > endHour) {
            break;
          }
        }
      }

      // Avanzar al siguiente día
      currentDate.setDate(currentDate.getDate() + 1);
    }

    return slots;
  }

  /**
   * Obtener el nombre del día de la semana
   */
  getDayName(dayOfWeek: number): string {
    const days = ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'];
    return days[dayOfWeek];
  }

  /**
   * Guardar la configuración de horarios en el backend
   */
  saveDoctorSchedule(doctorId: string, schedule: DoctorSchedule): Observable<string> {
    // El backend puede responder texto plano con HTTP 200; evitamos parseo JSON forzado.
    return this.http.post(`${this.apiUrl}/${doctorId}/configureSchedule`, schedule, {
      responseType: 'text'
    });
  }

  /**
   * Obtener la configuración de horarios de un doctor
   */
  getDoctorSchedule(doctorId: string): Observable<DoctorSchedule> {
    return this.http.get<DoctorSchedule>(`${this.apiUrl}/${doctorId}/schedule`);
  }
}
