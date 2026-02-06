import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { RegistrationRequest } from './registration-request.model';
import { Router, Routes } from '@angular/router';
import { APP_CONFIG } from '../../../core/config/app-config.token';
import { environment } from '../../../../environements.ts/environment';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RegistrationService {
  private readonly http: HttpClient = inject(HttpClient);
  private readonly config: typeof environment = inject(APP_CONFIG);

  private readonly REGISTRATION_URL = `${this.config.baseUrl}/users/register`;

  register(userData: RegistrationRequest) {
    return this.http.post<any>(this.REGISTRATION_URL, userData);
  }
}
