import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CartService } from '../cart.service';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { User } from 'src/app/infrastructure/auth/model/user.model';

@Component({
  selector: 'xp-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit, OnDestroy {

  selectedType: string = 'personal';
  startUrl: string = 'http://localhost:4200/';
  cartItems: any[] = [];
  cartCount: number = 0;
  user!: User;
  dropdownOpen: boolean = false;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private cartService: CartService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {

    this.authService.user$.subscribe((user) => {
      this.user = user;
    });
    
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
