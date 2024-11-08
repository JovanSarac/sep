import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartItemsSubject = new BehaviorSubject<any[]>([]);

  cartItems$ = this.cartItemsSubject.asObservable();

  constructor() {
    this.loadCartFromLocalStorage();
  }

  private loadCartFromLocalStorage(): void {
    const savedCart = localStorage.getItem('cart');
    const cartItems = savedCart ? JSON.parse(savedCart) : [];
    this.cartItemsSubject.next(cartItems);
  }

  addItem(item: any): void {
    const currentCartItems = this.cartItemsSubject.getValue();
    currentCartItems.push(item);
    this.updateCartState(currentCartItems);
  }

  getCartItems(): any[] {
    return this.cartItemsSubject.getValue();
  }

  removeItem(itemId: any): void {
    const updatedCart = this.cartItemsSubject.getValue().filter(item => item.id !== itemId);
    this.updateCartState(updatedCart);
  }
  
  private updateCartState(updatedCart: any[]): void {
    this.cartItemsSubject.next(updatedCart);
    localStorage.setItem('cart', JSON.stringify(updatedCart));
  }
}
