import { Component, OnInit } from '@angular/core';
import { AuthService } from './infrastructure/auth/auth.service';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'VivoNet';

  // constructor(
  //   private authService: AuthService,
  //   public router: Router
  // ) {}
  constructor(
    private keycloak: KeycloakService,
    private authService: AuthService,
    public router: Router
  ) {}


  async ngOnInit(): Promise<void> {
    // console.log("check user")
    // this.checkIfUserExists();
    const loggedIn: boolean = this.keycloak.isLoggedIn(); // boolean direktno
    if (loggedIn) {
      const token: string = await this.keycloak.getToken(); // getToken je Promise
      this.authService.saveToken(token); // pozivaš svoju metodu za čuvanje tokena
    }
  }
  
  private checkIfUserExists(): void {
    this.authService.checkIfUserExists();
  }
}
