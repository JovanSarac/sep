import { Component, OnInit } from '@angular/core';
import { ServiceOfferingsService } from '../service-offerings.service';
import { Service } from '../model/services.model';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-mobile',
  templateUrl: './mobile.component.html',
  styleUrls: ['./mobile.component.css']
})
export class MobileComponent implements OnInit {

  constructor(
    private service: ServiceOfferingsService,
    private activatedRoute: ActivatedRoute,
    private toastr: ToastrService
  ) { }

  mobileServices: Service[] = [];
  cartItems: any[] = [];
  cartCount: number = 0;

  ngOnInit(): void {
    this.loadCartFromLocalStorage();
    this.activatedRoute.queryParams.subscribe(params => {
      if (params['type'] === 'business') {
        this.mobileServices = this.service.getMobileServices();
      } else {
        this.mobileServices = this.service.getMobileServices();
      }
    });
  }

  addToCart(tariffPlan: any): void {
    this.cartItems.push(tariffPlan);
    this.toastr.success('Successfully added the ' + tariffPlan.name + ' service to the cart.', 'Success');
    this.updateLocalStorage();
    this.updateCartCount();
  }

  loadCartFromLocalStorage(): void {
    const savedCart = localStorage.getItem('cart');
    if (savedCart) {
      this.cartItems = JSON.parse(savedCart);
      this.updateCartCount();
    }
  }

  updateLocalStorage(): void {
    localStorage.setItem('cart', JSON.stringify(this.cartItems));
    window.dispatchEvent(new StorageEvent('storage', {
      key: 'cart',
      newValue: JSON.stringify(this.cartItems),
      oldValue: null,
      url: window.location.href
    }));
  }

  updateCartCount(): void {
    this.cartCount = this.cartItems.length;
  }
}
