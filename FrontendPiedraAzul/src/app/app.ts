import { Component, inject, signal, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './features/users/services/auth.service';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  private readonly authService = inject(AuthService);
  private readonly platformId = inject(PLATFORM_ID);
  protected readonly title = signal('FrontendPiedraAzul');
  private refreshTimerId: number | null = null;

  constructor() {
    if (isPlatformBrowser(this.platformId)) {
      this.authService.restoreSession().subscribe({
        error: (error) => {
          console.warn('[App] Could not restore user session from stored token.', error);
        }
      });
    }

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
