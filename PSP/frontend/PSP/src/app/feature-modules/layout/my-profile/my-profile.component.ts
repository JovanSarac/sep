import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { User } from 'src/app/infrastructure/auth/model/user.model';
import { LayoutService } from '../layout.service';
import { UserInfo } from '../model/userinfo';
import { SubscriptionDto } from '../../payments/model/subscription.model';
import { PaymentsService } from '../../payments/payments.service';

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrl: './my-profile.component.css'
})
export class MyProfileComponent implements OnInit{
  user!: User;
  userInfo!: UserInfo;
  selectedTab: string = 'personal'
  subscriptions: SubscriptionDto[]= [];

  constructor(
    private authService: AuthService,
    private layoutService: LayoutService,
    private paymentService: PaymentsService
  ){}

  ngOnInit(): void {
    this.authService.user$.subscribe((user) => {
      this.user = user;
    });

    this.layoutService.getUserInfoById(this.user.id).subscribe({
      next: (result) =>{
        this.userInfo = result;
        this.paymentService.getSubscriptionsForUser(this.user.id).subscribe({
          next:(result)=>{
            this.subscriptions = result
            console.log(this.subscriptions)
          }
        })
      }
    });

    
  }

  changeTab(tab: string){
    this.selectedTab = tab;
  }

  onLogout(): void {
    this.authService.logout();
  }
}
