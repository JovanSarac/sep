import { Component, OnInit } from '@angular/core';
import { getKeycloak } from 'src/app/infrastructure/auth/init/keycloak-init.factory';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  ngOnInit() {
    const kc = getKeycloak();
    const idTokenParsed = kc.idTokenParsed;
    const accessToken = kc.token;
    const username = kc.idTokenParsed?.['preferred_username']

    console.log('id token parsed: ', idTokenParsed);
    console.log('access token: ', accessToken)
    console.log('username: ', username)
    window.scrollTo(0, 0);
  }
}