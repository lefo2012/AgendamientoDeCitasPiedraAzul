import { Component, inject, signal } from '@angular/core';
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
  protected readonly title = signal('FrontendPiedraAzul');

  constructor() {
    this.authService.restoreSession().subscribe({
      error: (error) => {
        console.warn('[App] Could not restore user session from stored token.', error);
      }
    });
  }
}
