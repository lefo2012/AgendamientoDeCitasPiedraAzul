import { ChangeDetectorRef, Component, Inject, OnDestroy, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConfigDoctor } from '../../models/ConfigDoctor';
import { DoctorService } from '../../../appointments/services/doctor.service';
import { SelectionModel } from '@angular/cdk/collections';
import { Subscription } from 'rxjs/internal/Subscription';

@Component({
  selector: 'app-configure-doctors',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatCheckboxModule,
  ],
  templateUrl: './configure-doctors.html',
  styleUrl: './configure-doctors.scss',
})
export class ConfigureDoctors implements OnInit, OnDestroy {
  doctors: ConfigDoctor[] = [];
  filteredDoctors: ConfigDoctor[] = [];
  displayedColumns = ['select', 'firstName', 'lastName', 'identificationNumber'];

  searchTerm = '';
  selectedDoctorId: number | null = null;
  selectedDoctor: ConfigDoctor | null = null;
  showResults = false;
  selection = new SelectionModel<ConfigDoctor>(false, []);
  private activeSearchSub: Subscription | null = null;
  private pendingSearchTimeout: any;

  constructor(
    @Inject(PLATFORM_ID) private readonly platformId: object,
    private readonly doctorService: DoctorService,
    private readonly snackBar: MatSnackBar,
    private readonly cdr: ChangeDetectorRef,
    private readonly router: Router,
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.loadDoctors();
    }
  }

  ngOnDestroy(): void {
    this.activeSearchSub?.unsubscribe();
    if (this.pendingSearchTimeout) {
      clearTimeout(this.pendingSearchTimeout);
      this.pendingSearchTimeout = undefined;
    }
  }

  loadDoctors(): void {
    this.doctorService.getAllConfigurableDoctors().subscribe({
      next: (doctors) => {
        setTimeout(() => {
          this.doctors = doctors;
          this.filteredDoctors = doctors;
          this.showResults = true;
          this.cdr.detectChanges();
        }, 0);
      },
      error: () => {
        this.snackBar.open('No se pudieron cargar los médicos.', 'Cerrar', { duration: 3000 });
        this.showResults = true;
      },
    });
  }

  onSearchChange(): void {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) {
      this.filteredDoctors = this.doctors;
      return;
    }
    this.filteredDoctors = this.doctors.filter(
      (d) =>
        d.firstName.toLowerCase().includes(term) ||
        d.lastName.toLowerCase().includes(term) ||
        d.identificationNumber.toLowerCase().includes(term),
    );
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.filteredDoctors = this.doctors;
    this.selectedDoctorId = null;
    this.selectedDoctor = null;
  }

  toggleSelection(doctor: ConfigDoctor): void {
    if (this.selectedDoctorId === doctor.id) {
      this.setSelectedDoctor(null);
      return;
    }
    this.setSelectedDoctor(doctor);
  }

  private setSelectedDoctor(doctor: ConfigDoctor | null): void {
    this.selection.clear();
    if (!doctor) {
      this.selectedDoctorId = null;
      this.selectedDoctor = null;
      return;
    }
    this.selection.select(doctor);
    this.selectedDoctorId = doctor.id ?? null;
    this.selectedDoctor = doctor;
  }

  goToEditDoctor(): void {
    if (!this.selectedDoctorId || !this.selectedDoctor) return;
    this.router.navigate(['/admin/editar-medico', this.selectedDoctorId], {
      queryParams: {
        identificationNumber: this.selectedDoctor.identificationNumber,
        firstName: this.selectedDoctor.firstName,
        lastName: this.selectedDoctor.lastName,
      },
    });
  }
}
