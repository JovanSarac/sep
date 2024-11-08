import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CartService } from '../cart.service';

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
    private activatedRoute: ActivatedRoute,
    private cartService: CartService
  ) { }

  ngOnInit(): void {
    
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
    this.router.navigate(['/login']);
  }

  goToHome() {
    this.router.navigate(['']);
  }

  goToCart(){
    this.router.navigate(['cart']);
  }


}
