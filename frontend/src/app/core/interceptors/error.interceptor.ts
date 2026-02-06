import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { LoginService } from '../../features/auth/login/login.service';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService: LoginService = inject(LoginService);
  
  return next(req);
};
