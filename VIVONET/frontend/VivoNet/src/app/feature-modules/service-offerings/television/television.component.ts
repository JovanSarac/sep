import { Component } from '@angular/core';
import { ServiceOfferingsService } from '../service-offerings.service';
import { ActivatedRoute } from '@angular/router';
import { CartService } from '../../layout/cart.service';
import { ToastrService } from 'ngx-toastr';
import { Service, TypeService, TypeUser } from '../model/services.model';
import { CartItem } from '../model/cartitem.model';

@Component({
  selector: 'app-television',
  templateUrl: './television.component.html',
  styleUrl: './television.component.css'
})
export class TelevisionComponent {
  constructor(
    private service: ServiceOfferingsService,
    private activatedRoute: ActivatedRoute,
    private cartService: CartService,
    private toastr: ToastrService

  ){}

  televisionServices : Service[] = [];
  cartItems: any[] = [];
  cartCount: number = 0;

  ngOnInit(): void {
    this.cartService.cartItems$.subscribe(items => {
      this.cartCount = items.length;
    });

    this.activatedRoute.queryParams.subscribe(params => {
      if (params['type'] === 'business') {
        this.service.getTelevisionServicesBusiness().subscribe({
          next: (result)=>{
            this.televisionServices = result
          }
        });
      }else{
        this.service.getTelevisionServicesPersonal().subscribe({
          next: (result)=>{
            this.televisionServices = result
          }
        });
      }
    });
  }

  addToCart(tariffPlan: any, televisionServiceId: number, typeUser: TypeUser, typeService: TypeService): void {
    const cartItem: CartItem = {
        ...tariffPlan,
        mobileServiceId: televisionServiceId,
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

  getTypeUserLabel(typeUser: TypeUser | string | undefined): string {
    return typeUser === 'PERSONAL' ? 'Personal' : 'Business';
  }
}
