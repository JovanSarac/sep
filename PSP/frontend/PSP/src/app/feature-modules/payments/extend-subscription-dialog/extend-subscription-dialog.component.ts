import { Component, Inject, OnInit } from '@angular/core';
import { User } from 'src/app/infrastructure/auth/model/user.model';
import { PaymentService } from '../model/payment_service.model';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { PaymentsService } from '../payments.service';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { ToastrService } from 'ngx-toastr';
import { SubscriptionRequest } from '../model/subscription-request.model';
import { SubscriptionDto } from '../model/subscription.model';

@Component({
  selector: 'app-extend-subscription-dialog',
  templateUrl: './extend-subscription-dialog.component.html',
  styleUrl: './extend-subscription-dialog.component.css'
})
export class ExtendSubscriptionDialogComponent implements OnInit {

  user!: User;
  subscription! : SubscriptionDto;
  fullPrice: number = 0;
  duration: number = 0;

  subscribeForm = new FormGroup({
    subscriptionDuration: new FormControl('', [Validators.required, Validators.pattern('^[1-9][0-9]*$')]),    
  });

  constructor(
    public dialogRef: MatDialogRef<ExtendSubscriptionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: SubscriptionDto,
    private service : PaymentsService,
    private authService : AuthService,
    private toastr: ToastrService
  ) {}


  ngOnInit(): void {
    this.user = this.authService.user$.value;
    this.subscription = this.data;
    console.log(this.subscription.endDate)
    this.subscribeForm.controls.subscriptionDuration.valueChanges.subscribe((value) => {
      this.duration = Number(value);
      if (this.subscribeForm.controls.subscriptionDuration.valid && !isNaN(this.duration)) {
        this.fullPrice = parseFloat((this.duration * this.data.service.monthlyFee).toFixed(2));
      } else {
        this.fullPrice = 0;
      }
    });
  }

  closeDialog(){
    this.dialogRef.close();
  }

  extend(){
    this.markAllControlsAsTouched();

    if (this.subscribeForm.invalid) {
      this.toastr.error('Please review your entries. Some fields are incorrectly filled or missing.', 'Error');
      return;
    }

    this.subscription.totalCost += this.fullPrice;
    const calculatedEndDate = this.calculateEndDate(this.subscription.endDate, this.duration);
    this.subscription.endDate = this.prepareEndDateForBackend(calculatedEndDate) as unknown as Date;

    this.subscription.subscriptionDuration += this.duration;

    console.log(this.subscription)

    this.service.updateSubscription(this.subscription).subscribe({
      next:(result)=>{
        console.log(result)
        this.toastr.success('Subscription updated successfully!', 'Success');
        this.dialogRef.close(true);
      }
    })

  }

  private markAllControlsAsTouched(): void {
    Object.values(this.subscribeForm.controls).forEach((control) => {
      control.markAsTouched();
    });
  }

  calculateEndDate(startDate: Date, durationMonths: number): Date {
    const endDate = new Date(startDate);
    endDate.setMonth(endDate.getMonth() + durationMonths);

    return endDate;
  }

  convertDateToString(endDate: Date): string {
    const day = String(endDate.getDate()).padStart(2, '0');
    const month = String(endDate.getMonth() + 1).padStart(2, '0');
    const year = endDate.getFullYear();

    return `${day}.${month}.${year}`;
  } 

  prepareEndDateForBackend(date: Date): number[] {
    return [date.getFullYear(), date.getMonth() + 1, date.getDate()];
  }
}
