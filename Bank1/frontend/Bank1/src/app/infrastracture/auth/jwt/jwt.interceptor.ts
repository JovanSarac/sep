import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { KeycloakService } from 'src/app/services/keycloakservice';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private keycloakService: KeycloakService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Proveri da li je korisnik autentifikovan
    if (this.keycloakService.isAuthenticated()) {
      const token = this.keycloakService.getToken();
      
      if (token) {
        // Kloniraj zahtev i dodaj Authorization header
        const authReq = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`
          }
        });
        
        return next.handle(authReq);
      }
    }
    
    // Ako nema tokena, prosledi originalnu zahtev
    return next.handle(req);
  }
}