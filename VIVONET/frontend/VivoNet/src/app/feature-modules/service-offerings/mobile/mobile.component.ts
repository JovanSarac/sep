import { Component, OnInit } from '@angular/core';
import { ServiceOfferingsService } from '../service-offerings.service';
import { Service, TypeService, TypeUser } from '../model/services.model';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CartService } from '../../layout/cart.service';
import { CartItem } from '../model/cartitem.model';

@Component({
  selector: 'app-mobile',
  templateUrl: './mobile.component.html',
  styleUrls: ['./mobile.component.css']
})
export class MobileComponent implements OnInit {

  constructor(
    private service: ServiceOfferingsService,
    private activatedRoute: ActivatedRoute,
    private toastr: ToastrService,
    private cartService: CartService
  ) { }

  mobileServices: Service[] = [];
  cartItems: any[] = [];
  cartCount: number = 0;

  ngOnInit(): void {
    this.cartService.cartItems$.subscribe(items => {
      this.cartCount = items.length;
    });

    this.activatedRoute.queryParams.subscribe(params => {
      if (params['type'] === 'business') {
        this.mobileServices = this.service.getMobileServices();
      } else {
        this.mobileServices = this.service.getMobileServices();
      }
    });
  }


  addToCart(tariffPlan: any, mobileserviceId: number, typeUser: TypeUser, typeService: TypeService): void {
    const cartItem: CartItem = {
        ...tariffPlan,
        mobileServiceId: mobileserviceId,
        typeUser: typeUser,
        typeService: typeService
    };
    
    const exists = this.cartService.getCartItems().some((item: any) => item.id === cartItem.id);

    if (exists) {
      this.toastr.warning('This service is already in the cart.', 'Warning');
    } else {
      this.cartService.addItem(cartItem);
      this.toastr.success('Successfully added the ' + cartItem.name + ' service to the cart.', 'Success');
    }
  }
}
