import { Component, OnInit } from '@angular/core';
import { PaymentService } from '../model/payment_service.model';
import { PaymentsService } from '../payments.service';
import { SubscriptionDialogComponent } from '../subscription-dialog/subscription-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { SubscriptionDto } from '../model/subscription.model';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { User } from 'src/app/infrastructure/auth/model/user.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-services',
  templateUrl: './services.component.html',
  styleUrl: './services.component.css'
})
export class ServicesComponent implements OnInit{

  paymentServices!: PaymentService[];
  yourSubscriptions: SubscriptionDto[] = [];
  user!: User;

  constructor(
    private service: PaymentsService,
    private dialog: MatDialog,
    private authService: AuthService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.authService.user$.subscribe((user) => {
      this.user = user;
    });

    this.service.getActivePaymentServices().subscribe({
      next: (result)=>{
        this.paymentServices = result;
      }
    });

    this.service.getSubscriptionsForUser(this.user.id).subscribe({
      next:(result)=>{
        this.yourSubscriptions = result
      }
    });
  }

  OnSubscribe(paymentService:PaymentService){
    const alreadySubscribed = this.yourSubscriptions.some(
      (subscription) => subscription.service.id === paymentService.id
    );
  
    if (alreadySubscribed) {
      this.toastr.warning('This service is already in your active subscriptions.', 'Warning');
      
      return;
    }
    let dialogRef = this.dialog.open(SubscriptionDialogComponent, {
      width: '800px',
      height: '800px',
      data: paymentService,
      panelClass: 'custom-modalbox',
    });
  }

}
