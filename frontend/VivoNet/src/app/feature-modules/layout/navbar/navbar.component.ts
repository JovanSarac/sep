import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'xp-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  constructor(private router: Router){

  }
  selectedType: string = 'personal';

  selectType(type: string): void {
    this.selectedType = type;
  }

  goToLogin(){
    this.router.navigate(['/login']);
  }
}
