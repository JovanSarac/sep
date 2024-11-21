import { Component, OnInit } from '@angular/core';
import { ServiceOfferingsService } from '../service-offerings.service';
import { ActivatedRoute } from '@angular/router';
import { Service, TypeService, TypeUser } from '../model/services.model';
import { CartService } from '../../layout/cart.service';
import { CartItem } from '../model/cartitem.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-landline',
  templateUrl: './landline.component.html',
  styleUrl: './landline.component.css'
})
export class LandlineComponent implements OnInit{
  constructor(
    private service: ServiceOfferingsService,
    private activatedRoute: ActivatedRoute,
    private cartService: CartService,
    private toastr: ToastrService

  ){}

  landlineServices : Service[] = [];
  cartItems: any[] = [];
  cartCount: number = 0;

  ngOnInit(): void {
    this.cartService.cartItems$.subscribe(items => {
      this.cartCount = items.length;
    });

    this.activatedRoute.queryParams.subscribe(params => {
      if (params['type'] === 'business') {
        this.service.getLandlineServicesBusiness().subscribe({
          next: (result)=>{
            this.landlineServices = result
          }
        });
      }else{
        this.service.getLandlineServicesPersonal().subscribe({
          next: (result)=>{
            this.landlineServices = result
          }
        });
      }
    });
  }

  addToCart(tariffPlan: any, landlineserviceId: number, typeUser: TypeUser, typeService: TypeService): void {
    const cartItem: CartItem = {
        ...tariffPlan,
        mobileServiceId: landlineserviceId,
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
