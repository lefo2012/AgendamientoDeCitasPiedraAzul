import { Component } from '@angular/core';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { RouterLink } from '@angular/router';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    RouterLink
  ],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})

export class Navbar {
  constructor(private router: Router) {}
  
  scrollToCards() {
    if (this.router.url === '/') {
      // Ya estamos en home, scroll directo
      this.performScroll();
    } else {
      // No estamos en home, navega primero
      this.router.navigate(['/'], { fragment: 'appointments-options' })
        .then(() => {
          setTimeout(() => {
            this.performScroll();
          }, 300);
        });
    }
  }

  private performScroll() {
    const el = document.getElementById('appointments-options');
    if (el) {
      const yOffset = el.offsetTop - 100;
      window.scrollTo({
        top: yOffset,
        behavior: 'smooth'
      });
    }
  }
  
  backToHome() {
    window.location.href = '/';
  }
}
