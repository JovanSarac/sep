import { Component, OnInit } from '@angular/core';
import { ServiceOfferingsService } from '../service-offerings.service';
import { ActivatedRoute } from '@angular/router';
import { CartService } from '../../layout/cart.service';
import { ToastrService } from 'ngx-toastr';
import { Service, TypeService, TypeUser } from '../model/services.model';
import { CartItem } from '../model/cartitem.model';

@Component({
  selector: 'app-internet',
  templateUrl: './internet.component.html',
  styleUrl: './internet.component.css'
})
export class InternetComponent implements OnInit{
  constructor(
    private service: ServiceOfferingsService,
    private activatedRoute: ActivatedRoute,
    private cartService: CartService,
    private toastr: ToastrService

  ){}

  internetServices : Service[] = [];
  cartItems: any[] = [];
  cartCount: number = 0;

  ngOnInit(): void {
    this.cartService.cartItems$.subscribe(items => {
      this.cartCount = items.length;
    });

    this.activatedRoute.queryParams.subscribe(params => {
      if (params['type'] === 'business') {
        this.service.getInternetServicesBusiness().subscribe({
          next: (result)=>{
            this.internetServices = result
          }
        });
      }else{
        this.service.getInternetServicesPersonal().subscribe({
          next: (result)=>{
            this.internetServices = result
          }
        });
      }
    });
  }

  addToCart(tariffPlan: any, internetServiceId: number, typeUser: TypeUser, typeService: TypeService): void {
    const cartItem: CartItem = {
        ...tariffPlan,
        mobileServiceId: internetServiceId,
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
