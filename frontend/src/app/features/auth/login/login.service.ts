import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { computed, inject, Injectable, Signal, signal, WritableSignal } from '@angular/core';
import { LoginRequest } from './login-request.model';
import { environment } from '../../../../environements.ts/environment';
import { APP_CONFIG } from '../../../core/config/app-config.token';
import { Router } from '@angular/router';
import { LoginResponse } from './login-response.model';
import { catchError, Observable, of, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  private readonly http: HttpClient = inject(HttpClient);
  private readonly router: Router= inject(Router); 
  private readonly config: typeof environment = inject(APP_CONFIG);  
  private readonly LOGIN_PATH = `${this.config.baseUrl}/auth/login`;
  private readonly LOGOUT_PATH = `${this.config.baseUrl}/auth/logout`;
  private readonly REFRESH_PATH = `${this.config.baseUrl}/auth/refresh`;



  public jwtToken: WritableSignal<string | null> = signal<string | null>(null); 

  public readonly isAuthenticated: Signal<boolean>  = computed(() => !!this.jwtToken()); 

  login(userData: LoginRequest) :Observable<LoginResponse>{
    return this.http.post<LoginResponse>(this.LOGIN_PATH, userData)
    .pipe(
      tap(res =>this.jwtToken.set(res.accesstoken))
    );
  }

  logout(){
    this.http.post<string>(this.LOGOUT_PATH, null).subscribe({
      next: () =>this.clearSession(),
      error: (err: HttpErrorResponse)=>{
        console.log("Logout error ", err.message);
      }
    });
  }

  clearSession(){
      this.router.navigate(['/']);
    this.jwtToken.set(null);
  }

  refreshToken():Observable<LoginResponse | null>{
    return this.http.post<LoginResponse>(
      this.REFRESH_PATH, 
      null,
      {withCredentials: true}
    )
    .pipe(
      tap(res => this.jwtToken.set(res.accesstoken)),
      catchError(() =>{
        this.logout();
        return of(null);
      })
    )
  }
}
