import { Component, OnInit } from '@angular/core';
import { PaymentService } from '../model/payment_service.model';
import { PaymentsService } from '../payments.service';
import { SubscriptionDialogComponent } from '../subscription-dialog/subscription-dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-services',
  templateUrl: './services.component.html',
  styleUrl: './services.component.css'
})
export class ServicesComponent implements OnInit{

  paymentServices!: PaymentService[];

  constructor(
    private service: PaymentsService,
    private dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    this.service.getActivePaymentServices().subscribe({
      next: (result)=>{
        this.paymentServices = result;
      }
    })
  }

  OnSubscribe(paymentService:PaymentService){
    let dialogRef = this.dialog.open(SubscriptionDialogComponent, {
      width: '800px',
      height: '800px',
      data: paymentService,
      panelClass: 'custom-modalbox',
    });
  }

}
