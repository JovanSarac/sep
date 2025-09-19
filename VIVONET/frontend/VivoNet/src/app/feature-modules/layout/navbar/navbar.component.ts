import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CartService } from '../cart.service';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { User } from 'src/app/infrastructure/auth/model/user.model';
import { KeycloakService } from 'src/app/services/keycloakservice';
import { UserHelperService } from 'src/app/services/user-helper-service';
import { LayoutService } from '../layout.service';
import { UserInfo } from '../model/userinfo';

@Component({
  selector: 'xp-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit, OnDestroy {
  selectedType: string = 'personal';
  startUrl: string = 'https://localhost:4200/';
  cartItems: any[] = [];
  cartCount: number = 0;
  user!: User;
  dropdownOpen: boolean = false;
  userInfo!: UserInfo;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private cartService: CartService,
    private authService: AuthService,
    private keycloakService: KeycloakService,
    private layoutService: LayoutService,
    private userHelper: UserHelperService
  ) { }

  ngOnInit(): void {
    this.initializeUser();

    // this.authService.user$.subscribe((user) => {
    //   if (user) {
    //     this.user = user;
    //   }
    // });
   
    this.activatedRoute.queryParams.subscribe(params => {
      if (params['type'] === 'business') {
        this.selectedType = 'business';
      }
    });

    this.cartService.cartItems$.subscribe(items => {
      this.cartCount = items.length;
    });
  }

  ngOnDestroy(): void {}

  private initializeUser(): void {
    const currentUser = this.userHelper.getCurrentUser();
    
    if (currentUser) {
      this.user = currentUser;
      
      // Učitaj dodatne korisničke informacije
      if (this.user.role == 'ROLE_ADMIN' || this.user.role == 'ROLE_PERSONAL_USER' || this.user.role == 'ROLE_BUSINESS_USER')
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

  // private initializeKeycloakUser(): void {
  //   if (this.keycloakService.isAuthenticated()) {
  //     const idToken = this.keycloakService.getIdTokenParsed();
  //     const username = this.keycloakService.getUsername();
     
  //     // Kreiraj objekat direktno (bez new) jer je User interface
  //     this.user = {
  //       id: parseInt(idToken?.id) || 0, // konvertuj u number jer User.id je number
  //       username: username || '',
  //       role: idToken?.realm_access?.roles?.[0] || 'user', // uzmi prvu ulogu ili default 'user'
  //     };
  //   }
  // }

  // Dodaj helper metode za lakše korišćenje u template-u
  isLoggedIn(): boolean {
    return this.keycloakService.isAuthenticated();
  }

  getUserDisplayName(): string {
    return this.user?.username || this.keycloakService.getUsername() || 'User';
  }

  selectType(type: string): void {
    this.selectedType = type;
    if (type === 'business') {
      this.router.navigate([], {
        relativeTo: this.activatedRoute,
        queryParams: { type: 'business' },
        queryParamsHandling: 'merge'
      });
    } else if (type === 'personal') {
      this.router.navigate([], {
        relativeTo: this.activatedRoute,
      });
    }
  }

  goToLogin() {
    this.dropdownOpen = false;
    this.router.navigate(['/login']);
  }

  goToHome() {
    this.dropdownOpen = false;
    this.router.navigate(['']);
  }

  goToCart(){
    this.dropdownOpen = false;
    this.router.navigate(['cart']);
  }

  showMenu(){
    this.dropdownOpen = !this.dropdownOpen;
  }

  onLogout(): void {
    this.dropdownOpen = false;
    this.authService.logout();
  }
}