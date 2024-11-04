import { Component } from '@angular/core';

@Component({
  selector: 'xp-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  selectedType: string = 'personal'; // Podrazumevano stanje

  selectType(type: string): void {
    this.selectedType = type; // Postavite izabrani tip
  }
}
