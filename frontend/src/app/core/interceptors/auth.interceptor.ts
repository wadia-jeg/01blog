import { HttpErrorResponse, HttpHandler, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from "@angular/common/http";
import { inject } from "@angular/core";
import { LoginService } from "../../features/auth/login/login.service";
import { catchError, switchMap, throwError } from "rxjs";


export const authInterceptor:  HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn) => {
    const authService: LoginService = inject(LoginService);     
    const token:string | null = authService.jwtToken();

    if (token != null){
        const clonedReq = req.clone({
            headers:req.headers.set('Authorization', `Bearer ${token}`)
        })
        return next(clonedReq);
    }

    return next(req).pipe(
    catchError((error: HttpErrorResponse)=>{
      if (error.status !== 401 || req.url.includes("/auth/login") || req.url.includes("/auth/register") ){
        return throwError(()=> error);
      }

      console.log("WAAAAAAAAAAAAAAAAAAAAAAAAAA TOKEN EXPIRED");

      return authService.refreshToken().pipe(
        switchMap((res) => {
          if (res){
            const clonedReq = req.clone({
                headers:req.headers.set('Authorization', `Bearer ${res.accesstoken}`)
            })

            authService.logout();
            return next(clonedReq);
          }
          return throwError(()=> error);  
        }),
        catchError((error) =>{
          authService.logout();
          return throwError(()=> error);  
        })
        
      )      
    })
  );;
}