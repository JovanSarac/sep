import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'ethereum-payment';

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
