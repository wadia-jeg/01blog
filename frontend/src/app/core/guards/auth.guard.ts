import { inject } from "@angular/core";
import {  CanActivateFn, Router } from "@angular/router";
import { LoginService } from "../../features/auth/login/login.service";

export const authGuard: CanActivateFn = (route, state) => {
    const loginService: LoginService= inject(LoginService); 
    const router: Router = inject(Router);
    
        if (loginService.isAuthenticated()){
            return true;
        }
        
        router.navigate(['/auth/signin']);
        return false;
}

/*
// A reusable piece of logic
const hasRole = (role: string) => {
  return () => inject(AuthService).role() === role;
};

// In your routes
{ 
  path: 'admin', 
  component: AdminComponent, 
  canActivate: [authGuard, hasRole('ADMIN')] 
}
*/