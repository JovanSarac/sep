import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";
import { ACCESS_TOKEN } from '../../../shared/constants';
import { Router } from "@angular/router";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private router: Router) {}

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

    return next.handle(request).pipe(
    catchError(err => {
      if (err.status === 401) {
        // npr. redirect na login stranicu
        this.router.navigate(['/login']);
        localStorage.removeItem(ACCESS_TOKEN);
      }
      return throwError(() => err);
    })
  );
  }
}