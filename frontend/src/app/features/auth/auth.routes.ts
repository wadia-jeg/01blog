import { Routes } from '@angular/router';
import { RegistrationComponent } from './registration/registration.component';
import { LoginComponent } from './login/login.component';

export const AUTH_ROUTES: Routes = [
    { path: 'signup', component: RegistrationComponent },
    { path: 'signin', component: LoginComponent },
];
