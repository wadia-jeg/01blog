import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { RegistrationComponent } from './features/auth/registration/registration.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RegistrationComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly title = signal('frontend');
}
