import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

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

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.loadCartFromLocalStorage();
    this.activatedRoute.queryParams.subscribe(params => {
      if (params['type'] === 'business') {
        this.selectedType = 'business'; 
      }
    });

    window.addEventListener('storage', this.syncCartState);
  }

  ngOnDestroy(): void {
    window.removeEventListener('storage', this.syncCartState);
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
    this.router.navigate(['/login']);
  }

  goToHome() {
    this.router.navigate(['']);
  }

  syncCartState = (event: StorageEvent) => {
    if (event.key === 'cart') {
      this.loadCartFromLocalStorage();
    }
  }

  loadCartFromLocalStorage(): void {
    const savedCart = localStorage.getItem('cart');
    if (savedCart) {
      this.cartItems = JSON.parse(savedCart);
      this.updateCartCount();
    }
  }

  updateCartCount(): void {
    this.cartCount = this.cartItems.length;
  }
}
