import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, Observable, switchMap, tap, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { TokenStorage } from './jwt/token.service';
import { environment } from 'src/env/environment';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Login } from './model/login.model';
import { AuthenticationResponse } from './model/authentication-response.model';
import { User } from './model/user.model';
import { Registration } from './model/registration.model';
import { ACCESS_TOKEN } from 'src/app/shared/constants';
import { UserHelperService } from 'src/app/services/user-helper-service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  user$ = new BehaviorSubject<User>({username: "", id: 0, role: "" });
  private jwtHelperService = new JwtHelperService();

  constructor(private http: HttpClient,
    private tokenStorage: TokenStorage,
    private router: Router,
    private userHelperService: UserHelperService) { }

  login(login: Login): Observable<AuthenticationResponse> {
    this.tokenStorage.clear();
    return this.http
      .post<AuthenticationResponse>(environment.apiHost + 'auth/login', login)
      .pipe(
        tap((authenticationResponse) => {
          this.tokenStorage.saveAccessToken(authenticationResponse.access_token);
          this.tokenStorage.saveRefreshToken(authenticationResponse.refresh_token);
          this.setUser();
        })
      );
  }


  register(
    registration: Registration
  ): Observable<string> {
    return this.http
      .post(
        environment.apiHost + 'auth/register',
        registration,
        { responseType: 'text' }
      );
  }

  logout(): void {
    this.router.navigate(['']).then(_ => {
      this.tokenStorage.clear();
      this.user$.next({username: "", id: 0, role: "" });
      }
    );
  }

  checkIfUserExists(): void {
    const accessToken = this.tokenStorage.getAccessToken();
    if (accessToken == null) {
      return;
    }
    if (this.jwtHelperService.isTokenExpired(accessToken)) {
      //this.logout();
      return;
    }
    this.setUser();
  }

  isLoggedIn(): boolean {
    const token = this.tokenStorage.getAccessToken();

    if (token && !this.jwtHelperService.isTokenExpired(token)) {
      return true; 
    }

    return false; 
  }

  private setUser(): void {
    const accessToken = this.tokenStorage.getAccessToken() || "";
     const decodedToken = this.jwtHelperService.decodeToken(accessToken);
     console.log(+decodedToken.id)
     console.log(decodedToken)
    // const user: User = {
    //   id: +decodedToken.id,
    //   username: decodedToken.username,
    //   roles: decodedToken.role,
    // };
    const user = this.userHelperService.getCurrentUser();
    //this.user$.next(user);
    if (user) {
      this.user$.next(user);
    } else {
      // Fallback na prazan user objekat ako Keycloak nije autentifikovao
      this.user$.next({username: "", id: 0, role: "" });
    }
  }

  saveToken(token: any): void {
    this.tokenStorage.saveAccessToken(token.access_token);
    this.tokenStorage.saveRefreshToken(token.refresh_token);
  }

  sendCode(login: Login): Observable<any> {
    return this.http.post(environment.apiHost + 'auth/send/code', login);
  }

  codeLogin(login: Login): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(environment.apiHost + 'auth/login/code', login).pipe(
        tap((authenticationResponse) => {
          this.tokenStorage.saveAccessToken(authenticationResponse.access_token);
          this.tokenStorage.saveRefreshToken(authenticationResponse.refresh_token);
          this.setUser();
        })
      );
  }

  refreshToken(refreshToken: string): Observable<{ accessToken: string }> {
    return this.http.post<{ accessToken: string }>(
      environment.apiHost + 'auth/refresh',
      { refreshToken }
    ).pipe(
        tap((authenticationResponse) => {
          this.tokenStorage.saveAccessToken(authenticationResponse.accessToken);
          this.setUser();
        })
      );
  }
}