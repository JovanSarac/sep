import { Injectable } from '@angular/core';
import {
  CanActivate,
  UrlTree,
  Router,
} from '@angular/router';
import { catchError, map, Observable, of } from 'rxjs';
import { AuthService } from './auth.service';
import { User } from './model/user.model';
import { REFRESH_TOKEN } from 'src/app/shared/constants';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  canActivate():
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
        
    const user: User = this.authService.user$.getValue();
    if (user.username === '') {
      this.router.navigate(['login']);
      return false;
    }
    if (this.authService.isLoggedIn()) {
      return true;
    } else {
      const refresh = localStorage.getItem(REFRESH_TOKEN)
      console.log(refresh)
      if(refresh){
        return this.authService.refreshToken(refresh).pipe(
          map(() => true), // refresh succeeded → allow navigation
          catchError(() => {
            // refresh failed → redirect to login
            localStorage.removeItem('ACCESS_TOKEN');
            localStorage.removeItem('REFRESH_TOKEN');
            this.router.navigate(['login']);
            return of(false)
          })
        );
      }

      
      return false;
    }
  }
}