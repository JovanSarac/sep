import { Component, OnInit } from '@angular/core';
import { CartService } from '../cart.service';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { User } from 'src/app/infrastructure/auth/model/user.model';
import { Route, Router } from '@angular/router';
import { LayoutService } from '../layout.service';
import { UserInfo } from '../model/userinfo';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent implements OnInit{
  cartItems: any[] = [];
  totalItems: number = 0;
  totalPrice: number = 0;
  user!: User;
  userInfo!: UserInfo;

  constructor(
    private cartService: CartService,
    private authService: AuthService,
    private router: Router,
    private layoutService: LayoutService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.authService.user$.subscribe((user) => {
      this.user = user;
      if(this.user.id != 0){
        this.layoutService.getUserInfoById(this.user.id).subscribe({
          next:(result)=>{
            this.userInfo = result;
          }
        });
      }
    });
    this.cartService.cartItems$.subscribe(items => {
      this.cartItems = items;
      this.calculateTotals();
    });
  }

  calculateTotals(): void {
    this.totalItems = this.cartItems.length;
    this.totalPrice = this.cartItems.reduce((sum, item) => sum + item.price, 0);
  }


  deleteFromCart(itemId: any): void {
    this.cartService.removeItem(itemId);
  }

  OnCheckout(){
    if(this.totalItems == 0){
      return;
    }
    if(this.user.id == 0){
      this.router.navigate(['/login']);
      return;
    }else{
      
      if(this.user.role === 'ROLE_BUSINESS_USER'){
        const hasBusinessOnlyService = this.cartItems.some(
          (item) => item.typeUser === 'PERSONAL'
        );

        if(hasBusinessOnlyService){
          this.toastr.warning("Your cart contains items available only for BUSINESS users. Please remove PERSONAL items them to proceed.","Warning");
          return;
        }

      }else if(this.user.role === 'ROLE_PERSONAL_USER'){
        const hasPersonalOnlyService = this.cartItems.some(
          (item) => item.typeUser === 'BUSINESS'
        );

        if(hasPersonalOnlyService){
          this.toastr.warning("Your cart contains items available only for PERSONAL users. Please remove BUSINESS items them to proceed.","Warning");
          return;
        }

      }
     
  
      

      const checkoutData = {
        userId: this.user.id,
        userFirstName: this.userInfo.firstName,
        userLastName: this.userInfo.lastName,
        userEmail: this.userInfo.email,
        cart: {
          items: this.cartItems.map(item => ({
            name: item.name,
            price: item.price,
          })),
          totalItems: this.totalItems,
          totalPrice: this.totalPrice,
        },
        webShopId: '5',
        webShopName: 'VivoNet',
        webShopUrl: window.location.origin,
        
      };

    
      this.cartService.checkingWebShopServices(checkoutData).subscribe({
        next: (redirectUrl) => {
          console.log('Redirect URL:', redirectUrl);
          window.location.href = redirectUrl;
          //window.open(redirectUrl, '_blank');
        },
        error: (error) => {
          console.error('Error:', error);
          alert('No active subscriptions found for this webshop:' + checkoutData.webShopName);
        },
      });
    }
  }
}

