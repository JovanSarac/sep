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
    private cartService: CartService,
  ){}

  ngOnInit(): void {
    this.status = this.route.snapshot.paramMap.get('status') || '';
    if (this.status === 'success') {
      this.route.queryParams.subscribe(params => {
        const orderId = params['token'];
        const payerId = params['PayerID'];

        this.cartService.capturePaypalOrder(orderId, payerId).subscribe({
          next: res => {
            console.log('Capture success:', res);
            this.cartService.clearCart();
          },
          error: err => {
            console.error('Capture failed:', err);
            this.router.navigate(['/transaction-status/failed']);
          }
        });
      });
    }

    if(this.status != 'success' && this.status != 'failed' && this.status != 'error' && this.status != 'cancel'){
      this.router.navigate(['/']);
    }

    if (this.status === 'success') {
      this.cartService.clearCart();
    }
  
  }

}
