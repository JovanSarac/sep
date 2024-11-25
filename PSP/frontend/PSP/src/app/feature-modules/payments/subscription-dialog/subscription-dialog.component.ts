import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { PaymentService } from '../model/payment_service.model';
import { PaymentsService } from '../payments.service';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { ToastrService } from 'ngx-toastr';
import { User } from 'src/app/infrastructure/auth/model/user.model';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { SubscriptionDto } from '../model/subscription.model';
import { SubscriptionRequest } from '../model/subscription-request.model';
import { ApiKeyDto } from '../model/api-key.model';

@Component({
  selector: 'app-subscription-dialog',
  templateUrl: './subscription-dialog.component.html',
  styleUrl: './subscription-dialog.component.css'
})
export class SubscriptionDialogComponent implements OnInit {

  user!: User;
  paymentService! : PaymentService;
  fullPrice: number = 0;
  duration: number = 0;

  subscribeForm = new FormGroup({
    subscriptionDuration: new FormControl('', [Validators.required, Validators.pattern('^[1-9][0-9]*$')]),    
  });

  constructor(
    public dialogRef: MatDialogRef<SubscriptionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: PaymentService,
    private service : PaymentsService,
    private authService : AuthService,
    private toastr: ToastrService
  ) {}


  ngOnInit(): void {
    this.user = this.authService.user$.value;
    this.paymentService = this.data;
    this.subscribeForm.controls.subscriptionDuration.valueChanges.subscribe((value) => {
      this.duration = Number(value);
      if (this.subscribeForm.controls.subscriptionDuration.valid && !isNaN(this.duration)) {
        this.fullPrice = parseFloat((this.duration * this.data.monthlyFee).toFixed(2));
      } else {
        this.fullPrice = 0;
      }
    });
  }

  closeDialog(){
    this.dialogRef.close();
  }

  subscribe(){
    this.markAllControlsAsTouched();

    if (this.subscribeForm.invalid) {
      this.toastr.error('Please review your entries. Some fields are incorrectly filled or missing.', 'Error');
      return;
    }

    const subscriptionDto : SubscriptionRequest = {
      userId: this.user.id,
      serviceId: this.data.id,
      subscriptionDuration: this.duration
    }

    this.service.createSubscription(subscriptionDto).subscribe({
      next: (result) => {
        this.toastr.success('Subscription created successfully!', 'Success');

        var apiKey : ApiKeyDto = {
          merchantId: result.merchantId!,
          merchantPassword: result.merchantPassword!,
          paymentType: -1
        }

        this.service.saveApiKey(apiKey).subscribe({});

        this.dialogRef.close(true);
      },
      error: (err) => {
        console.error('Error creating subscription:', err);
        this.toastr.error('Failed to create subscription. Please try again.', 'Error');
      }, 
    })

  }

  private markAllControlsAsTouched(): void {
    Object.values(this.subscribeForm.controls).forEach((control) => {
      control.markAsTouched();
    });
  }

}
