import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, from, Observable, switchMap, throwError } from "rxjs";
import { ACCESS_TOKEN, REFRESH_TOKEN } from '../../../shared/constants';
import { Router } from "@angular/router";
import { AuthService } from "../auth.service";
import { environment } from "src/env/environment";
//import { getKeyCloak } from "../keycloak-init";

// @Injectable()
// export class JwtInterceptor implements HttpInterceptor {
//   constructor(private router: Router, private authService: AuthService) {}

//   intercept(
//     request: HttpRequest<any>,
//     next: HttpHandler
//   ): Observable<HttpEvent<any>> {
//     // if (!request.url.startsWith(environment.apiHost))
//     // {
//     //   return next.handle(request);
//     // }

//     const keycloak = getKeyCloak();
//     if (!keycloak)
//     {
//       return next.handle(request);
//     }

//     const refresh = keycloak.updateToken ? keycloak.updateToken(30) : Promise.resolve(true);

//     return from(refresh).pipe(
//       switchMap(() => {
//         const token = keycloak.token 
//         const authRequest = token ? request.clone({setHeaders: {Authorization: 'Bearer ${token}'}}) : request;
//         if (request.url.startsWith(environment.apiHost)) {
//           console.log('[AuthInterceptor]', request.method, request.url, token ? 'ATTACHED' : 'NO TOKEN');
//         }
//         return next.handle(authRequest);
//       }),
//       catchError(() => next.handle(request))
//     )
//   }

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private router: Router, private authService: AuthService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const token = localStorage.getItem(ACCESS_TOKEN);
    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }
    console.log(token)
    console.log("interceptor")
    return next.handle(request).pipe(
      catchError(err => {
        console.log("catch")
        console.log(err)
        if(err.status === 401){
          console.log("401")
          return this.handleRefreshToken(request, next);
        }
        else if (err.status === 403) {
          console.log("403")
          this.router.navigate(['/login']);
          localStorage.removeItem(ACCESS_TOKEN);
        }
        console.log("other",err.status)
        return throwError(() => err);
      })
    );
  }

  private handleRefreshToken(req: HttpRequest<any>, next: HttpHandler) {
    const refreshToken = localStorage.getItem(REFRESH_TOKEN);
    if (!refreshToken) {
      // no refresh token, log out
      return throwError(() => new Error('No refresh token'));
    }

    return this.authService.refreshToken(refreshToken).pipe(
      switchMap(res => {
        // Save new access token
        localStorage.setItem(ACCESS_TOKEN, res.accessToken);

        // Retry original request with new token
        const clonedReq = req.clone({
          setHeaders: { Authorization: `Bearer ${res.accessToken}` }
        });

        return next.handle(clonedReq);
      }),
      catchError(err => {
        // Refresh failed → log out
        localStorage.removeItem(ACCESS_TOKEN);
        localStorage.removeItem(REFRESH_TOKEN);
        this.router.navigate(['/login']);
        return throwError(() => err);
      })
    );
  }
}

// import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
// import { Injectable } from "@angular/core";
// import { catchError, from, Observable, switchMap, throwError } from "rxjs";
// import { ACCESS_TOKEN, REFRESH_TOKEN } from '../../../shared/constants';
// import { Router } from "@angular/router";
// import { AuthService } from "../auth.service";
// import { environment } from "src/env/environment";
// import { getKeyCloak } from "../keycloak-init";

// @Injectable()
// export class JwtInterceptor implements HttpInterceptor {
//     constructor(private router: Router, private authService: AuthService) {}

//     intercept(
//         request: HttpRequest<any>,
//         next: HttpHandler
//     ): Observable<HttpEvent<any>> {
//         // Skip authentication for non-API requests
//         // if (!request.url.startsWith(environment.apiHost)) {
//         //     return next.handle(request);
//         // }
//         console.log('🔍 [INTERCEPTOR START]', {
//           url: request.url,
//           method: request.method,
//           headers: request.headers.keys()
//         });

//         const keycloak = getKeyCloak();
//         if (!keycloak || !keycloak.authenticated) {
//             console.log('[AuthInterceptor] No keycloak or not authenticated');
//             return next.handle(request);
//         }

//         // Use from() to convert Promise to Observable
//         const refresh = keycloak.updateToken ? keycloak.updateToken(30) : Promise.resolve(true);

//         return from(refresh).pipe(
//             switchMap((refreshed) => {
//                 const token = keycloak.token;
//                 console.log('[AuthInterceptor]', request.method, request.url, token ? 'ATTACHED' : 'NO TOKEN');
                
//                 if (token) {
//                     // Fix: Use backticks for template literal
//                     const authRequest = request.clone({
//                         setHeaders: { Authorization: `Bearer ${token}` }
//                     });
//                     return next.handle(authRequest);
//                 } else {
//                     console.warn('[AuthInterceptor] No token available');
//                     return next.handle(request);
//                 }
//             }),
//             catchError((error) => {
//                 console.error('[AuthInterceptor] Token refresh failed:', error);
                
//                 // If token refresh fails, try to login again
//                 if (error.error === 'login-required') {
//                     keycloak.login();
//                     return throwError(() => error);
//                 }
                
//                 // For other errors, proceed without token
//                 return next.handle(request);
//             })
//         );
//     }
// }