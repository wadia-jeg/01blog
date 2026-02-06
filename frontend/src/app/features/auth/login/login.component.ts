import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, computed, inject, signal, WritableSignal } from '@angular/core';
import { FormBuilder, NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { LoginService } from './login.service';
import { LoginRequest } from './login-request.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-form',
  imports: [MatCardModule, MatFormFieldModule, MatInputModule, ReactiveFormsModule],
  templateUrl: 'login.html',
  styleUrl: 'login.scss',
})
export class LoginComponent {
  private readonly loginService: LoginService = inject(LoginService);
  private fb: NonNullableFormBuilder = inject(NonNullableFormBuilder);
  private readonly router: Router= inject(Router); 
  private loginError: WritableSignal<string> = signal<string>('');
  

  private readonly passwordPattern = /^[A-Za-z0-9\-_\.*@]+$/;

  isLoading = false;

  loginForm = this.fb.group({
    login: [
      '',
      [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(10),
      ],
    ],
    password: [
      '',
      [
        Validators.required,
        Validators.pattern(this.passwordPattern),
        Validators.minLength(8),
        Validators.maxLength(20),
      ],
    ],
   
  });

  get f() {
    return this.loginForm.controls;
  }

  getLoginError(): string {
     let login = this.f.login;
    if (login.hasError('required')) return 'A valid username or email is required';
    if (login.hasError('pattern')) return 'Inavlid Login';
    return this.loginError();
  }

  

  getPasswordError(): string {
    let password = this.f.password;
    if (password.hasError('required')) return 'Avalid password is required';
    if (password.hasError('pattern')) return 'Inavlid password';
    return '';
  }


  onSubmit() {
    console.log('Form submitted', this.loginForm.value);
    if (this.loginForm.invalid) return;

      this.isLoading = true;
      let userData: LoginRequest = this.loginForm.getRawValue()  ;
      this.loginService.login(userData).subscribe({
        next: (res) => {
          console.log('Login response ', res);
          this.router.navigate(['/home'])
        },
        error: (err: HttpErrorResponse) => {
          console.log('Login error ', err.message);
          this.loginError.set(err.message);
        },
      });
  }
}
