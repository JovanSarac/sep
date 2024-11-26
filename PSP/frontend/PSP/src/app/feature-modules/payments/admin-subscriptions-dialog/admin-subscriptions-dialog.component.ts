import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { User } from 'src/app/infrastructure/auth/model/user.model';
import { UserInfo } from '../../layout/model/userinfo';
import { PaymentsService } from '../payments.service';
import { ToastrService } from 'ngx-toastr';
import { SubscriptionDto } from '../model/subscription.model';

@Component({
  selector: 'app-admin-subscriptions-dialog',
  templateUrl: './admin-subscriptions-dialog.component.html',
  styleUrl: './admin-subscriptions-dialog.component.css'
})
export class AdminSubscriptionsDialogComponent implements OnInit {

  userInfo! : UserInfo;
  activeSubscriptions: SubscriptionDto[] = [];

  
  constructor(
    public dialogRef: MatDialogRef<AdminSubscriptionsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: UserInfo,
    private service : PaymentsService,
    private toastr: ToastrService
  ) {}


  ngOnInit(): void {
    this.userInfo = this.data;
    this.service.getSubscriptionsForUserAdmin(this.userInfo.id).subscribe({
      next:(result)=>{
        this.activeSubscriptions = result;
      }
    });

  }

  closeDialog(){
    this.dialogRef.close();
  }

  deactivatePspService(subscription : SubscriptionDto){
    subscription.isActive = false;
    this.service.updateSubscription(subscription).subscribe({
      next: ()=>{
        this.ngOnInit()
      }
    })
  }

  activatePspService(subscription : SubscriptionDto){
    subscription.isActive = true;
    this.service.updateSubscription(subscription).subscribe({
      next: ()=>{
        this.ngOnInit()
      }
    })
  }


}
