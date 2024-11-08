import { Component, OnInit } from '@angular/core';
import { CartService } from '../cart.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent implements OnInit{
  cartItems: any[] = [];
  totalItems: number = 0;
  totalPrice: number = 0;

  constructor(private cartService: CartService) {}

  ngOnInit(): void {
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
}

