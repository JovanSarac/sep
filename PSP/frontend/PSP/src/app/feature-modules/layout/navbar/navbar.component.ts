import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { User } from 'src/app/infrastructure/auth/model/user.model';

@Component({
  selector: 'xp-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  startUrl: string = 'http://localhost:4201/';
  cartItems: any[] = [];
  cartCount: number = 0;
  selectedTab : string = '/';
  dropdownOpen: boolean = false;

  user!: User;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.authService.user$.subscribe((user) => {
      this.user = user;
    });
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.selectedTab = event.urlAfterRedirects;
      }
    });
    
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }

  goToHome() {
    this.router.navigate(['']);
  }

  showMenu(){
    this.dropdownOpen = !this.dropdownOpen;
  }

  onLogout(): void {
    this.dropdownOpen = false;
    this.authService.logout();
  }

  goToMyProfile(){
    this.router.navigate(['/my-profile']);
  }

}
