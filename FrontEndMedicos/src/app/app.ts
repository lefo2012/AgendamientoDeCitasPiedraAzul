import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from "@angular/router";
import { AuthService } from './features/users/services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class App {
  private readonly authService = inject(AuthService);
  protected readonly title = signal('FrontEndMedicos');
  private refreshTimerId: number | null = null;

  constructor() {
    this.authService.restoreSession().subscribe({
      error: (error) => {
        console.warn('[App] Could not restore user session from stored token.', error);
      }
    });

    this.setupSessionRefresh();
  }

  private setupSessionRefresh(): void {
    if (typeof window === 'undefined') {
      return;
    }

    const refreshIntervalMs = 5 * 60 * 1000;

    this.refreshTimerId = window.setInterval(() => {
      this.tryRefreshSession();
    }, refreshIntervalMs);

    document.addEventListener('visibilitychange', () => {
      if (document.visibilityState === 'visible') {
        this.tryRefreshSession();
      }
    });
  }

  private tryRefreshSession(): void {
    if (!this.authService.isAuthenticated()) {
      return;
    }

    this.authService.refreshAccessToken().subscribe({
      error: (error) => {
        console.warn('[App] Session refresh failed.', error);
      }
    });
  }
}
