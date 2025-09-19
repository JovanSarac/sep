import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { User } from 'src/app/infrastructure/auth/model/user.model';
import { UserInfo } from '../model/userinfo';
import { KeycloakService } from 'src/app/services/keycloakservice';
import { LayoutService } from '../layout.service';
import { UserHelperService } from 'src/app/services/user-helper-service';

@Component({
  selector: 'xp-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  startUrl: string = 'https://localhost:4201/';
  cartItems: any[] = [];
  cartCount: number = 0;
  selectedTab : string = '/';
  dropdownOpen: boolean = false;
  userInfo!: UserInfo;

  user!: User;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private authService: AuthService,
    private keycloakService: KeycloakService,
    private layoutService: LayoutService,
    private userHelper: UserHelperService
  ) { }

  ngOnInit(): void {
    this.initializeUser();
    // this.authService.user$.subscribe((user) => {
    //   this.user = user;
    // });
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.selectedTab = event.urlAfterRedirects;
      }
    });
  }

  private initializeUser(): void {
    const currentUser = this.userHelper.getCurrentUser();
    
    if (currentUser) {
      this.user = currentUser;
      
      //Učitaj dodatne korisničke informacije
      if (this.user.role == 'ROLE_WEB_SHOP')
      {
        if(this.user.id != 0){
          this.layoutService.getUserInfoById(this.user.id).subscribe({
            next:(result)=>{
              this.userInfo = result;
            },
            error: (error) => {
              console.error('Error loading user info:', error);
            }
          });
        }
      }
    }
  }

  isLoggedIn(): boolean {
    return this.keycloakService.isAuthenticated();
  }

  getUserDisplayName(): string {
    return this.user?.username || this.keycloakService.getUsername() || 'User';
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
    this.dropdownOpen = false;
    this.router.navigate(['/my-profile']);
  }

}
