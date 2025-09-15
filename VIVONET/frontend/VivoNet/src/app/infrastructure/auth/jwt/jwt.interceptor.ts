import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, switchMap, throwError } from "rxjs";
import { ACCESS_TOKEN, REFRESH_TOKEN } from '../../../shared/constants';
import { Router } from "@angular/router";
import { AuthService } from "../auth.service";
import { TokenRefreshRequest } from "../model/tokenRefreshRequest.model";

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

    const tokenRequest: TokenRefreshRequest = { refreshToken };
    return this.authService.refreshToken(tokenRequest).pipe(
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