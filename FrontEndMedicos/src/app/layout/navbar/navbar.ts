import { Component } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [MatToolbarModule, MatButtonModule, MatIconModule, MatMenuModule, RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar {
  constructor(private router: Router) {}

  scrollToCards() {
    if (this.router.url === '/') {
      this.performScroll();
      return;
    }

    this.router.navigate(['/'], { fragment: 'appointments-options' }).then(() => {
      setTimeout(() => this.performScroll(), 250);
    });
  }

  private performScroll() {
    const el = document.getElementById('appointments-options');
    if (!el) {
      return;
    }

    const yOffset = el.offsetTop - 100;
    window.scrollTo({
      top: yOffset,
      behavior: 'smooth',
    });
  }
}
