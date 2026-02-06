import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';

export const routes: Routes = [
    {path: 'auth', 
        loadChildren: () => import('./features/auth/auth.routes').then(f => f.AUTH_ROUTES)
    },
    {
        path:'home', component: HomeComponent
    },
    {
        path:'',
        redirectTo : 'auth/signup',
        pathMatch: 'full'
    }
];
