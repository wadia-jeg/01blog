import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { RegistrationService } from './registration.service';
import { RegistrationRequest } from './registration-request.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration-form',
  imports: [MatCardModule, MatFormFieldModule, MatInputModule, ReactiveFormsModule],
  templateUrl: 'registration.html',
  styleUrl: 'registration.scss',
})
export class RegistrationComponent {
  private readonly registrationService: RegistrationService = inject(RegistrationService);
  private fb: FormBuilder = inject(FormBuilder);
  private router: Router= inject(Router);

  private readonly emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  private readonly passwordPattern = /^[A-Za-z0-9\-_\.*@]+$/;
  private readonly namePattern = /^[A-Za-z]+$/;
  private readonly usernamePattern = /^[A-Za-z0-9\-_\.*]+$/;

  isLoading = false;

  registrationForm = this.fb.group({
    username: [
      '',
      [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(10),
        Validators.pattern(this.usernamePattern),
      ],
    ],
    email: ['', [Validators.required, Validators.pattern(this.emailPattern)]],
    password: [
      '',
      [
        Validators.required,
        Validators.pattern(this.passwordPattern),
        Validators.minLength(8),
        Validators.maxLength(20),
      ],
    ],
    firstName: [
      '',
      [Validators.minLength(3), Validators.maxLength(20), Validators.pattern(this.namePattern)],
    ],
    lastName: [
      '',
      [Validators.minLength(3), Validators.maxLength(20), Validators.pattern(this.namePattern)],
    ],
  });

  get f() {
    return this.registrationForm.controls;
  }

  getUsernameError(): string {
    let username = this.f.username;
    if (username.hasError('required')) return 'A Usernmae is required';
    if (username.hasError('minlength') || username.hasError('maxlength'))
      return 'Your Username must have between 3 to 10 charatcers';
    if (username.hasError('pattern'))
      return 'Your Username must have only contains (letters, numbers, _, -_.*)';
    return '';
  }

  getEmailError(): string {
    let email = this.f.email;
    if (email.hasError('required')) return 'Avalid Email is required';
    if (email.hasError('pattern')) return 'Inavlid Email';
    if (email.hasError('pattern'))
      return 'Your Username must have only contains (letters, numbers, _, -_.*)';
    return '';
  }

  getPasswordError(): string {
    let password = this.f.password;
    if (password.hasError('required')) return 'Avalid password is required';
    if (password.hasError('pattern')) return 'Inavlid password';
    return '';
  }
  getFirstNameError(): string {
    let firstName = this.f.firstName;
    if (firstName.hasError('minlength') || firstName.hasError('maxlength'))
      return 'Your FirstName must have between 3 to 10 charatcers';
    if (firstName.hasError('pattern')) return 'Your FirstName must have only contains letters';
    return '';
  }

  getLastNameError(): string {
    let lastName = this.f.lastName;
    if (lastName.hasError('minlength') || lastName.hasError('maxlength')) {
      return 'Your LastName must have between 3 to 10 charatcers';
    } else if (lastName.hasError('pattern')) {
      return 'Your LastName must have only contains letters';
    }
    return '';
  }

  onSubmit() {
    console.log('Form submitted', this.registrationForm.value);
    if (this.registrationForm.invalid) return;
      this.isLoading = true;
      let userData: RegistrationRequest = this.registrationForm.value as RegistrationRequest;
      this.registrationService.register(userData).subscribe({
        next: (res) => {
          this.isLoading = false;
          console.log('Registration response ', res);
          this.router.navigate(['auth/signin'])
        },
        error: (err: HttpErrorResponse) => {
          this.isLoading = false;
          console.log('Registration Error ', err.message);

        },
      });
    
  }
}
