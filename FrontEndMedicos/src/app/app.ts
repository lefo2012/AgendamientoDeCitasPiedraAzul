import { Component, signal } from '@angular/core';
import { MainLayout } from './layout/main-layout/main-layout';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [MainLayout],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class App {
  protected readonly title = signal('FrontEndMedicos');
}
