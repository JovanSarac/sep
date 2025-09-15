import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CartService } from '../cart.service';

@Component({
  selector: 'app-transaction-status',
  templateUrl: './transaction-status.component.html',
  styleUrl: './transaction-status.component.css'
})
export class TransactionStatusComponent implements OnInit {

  status : string = ''

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private cartService: CartService
  ){}

  ngOnInit(): void {
    this.status = this.route.snapshot.paramMap.get('status') || '';
    this.route.queryParams.subscribe(params => {
      console.log('PayPal token:', params['token']);
      console.log('PayerID:', params['PayerID']);
    });
    if(this.status != 'success' && this.status != 'failed' && this.status != 'error'){
      this.router.navigate(['/']);
    }

    if (this.status === 'success') {
      this.cartService.clearCart();
    }
  
  }

}
