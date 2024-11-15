import { Component, OnInit } from '@angular/core';
import { PaymentService } from '../model/payment_service.model';
import { PaymentsService } from '../payments.service';

@Component({
  selector: 'app-services',
  templateUrl: './services.component.html',
  styleUrl: './services.component.css'
})
export class ServicesComponent implements OnInit{

  paymentServices!: PaymentService[];

  constructor(
    private service: PaymentsService
  ) {}

  ngOnInit(): void {
    this.service.getActivePaymentServices().subscribe({
      next: (result)=>{
        this.paymentServices = result;
        console.log(this.paymentServices)
      }
    })
  }

}
