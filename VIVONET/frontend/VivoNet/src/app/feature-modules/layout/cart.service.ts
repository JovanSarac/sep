import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartItemsSubject = new BehaviorSubject<any[]>([]);

  cartItems$ = this.cartItemsSubject.asObservable();

  constructor(
    private http: HttpClient
  ) {
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

  clearCart(): void {
    const emptyCart: any[] = [];
    this.updateCartState(emptyCart);
  }
  
  private updateCartState(updatedCart: any[]): void {
    this.cartItemsSubject.next(updatedCart);
    localStorage.setItem('cart', JSON.stringify(updatedCart));
  }

  checkingWebShopServices(checkoutData: any): Observable<string> {
    return this.http.post<string>('http://localhost:8090/api/checking_webshop_services', checkoutData, {
      responseType: 'text' as 'json', // Angular očekuje JSON, ali specificiramo tekst
    });
  }
}
