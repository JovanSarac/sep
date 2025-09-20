import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Bank1';

  constructor(
    private keycloak: KeycloakService,
    public router: Router
  ) {}

  async ngOnInit(): Promise<void> {
    //this.checkIfUserExists();
    const loggedIn: boolean = await this.keycloak.isLoggedIn(); // boolean direktno
    if (loggedIn) {
      const token: string = await this.keycloak.getToken(); // getToken je Promise
    }
  }
}
