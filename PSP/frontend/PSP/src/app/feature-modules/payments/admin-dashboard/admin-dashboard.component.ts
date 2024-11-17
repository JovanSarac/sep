import { Component, OnInit } from '@angular/core';
import { PaymentsService } from '../payments.service';
import { UserInfo } from '../../layout/model/userinfo';
import { User } from 'src/app/infrastructure/auth/model/user.model';
import { MatDialog } from '@angular/material/dialog';
import { AdminSubscriptionsDialogComponent } from '../admin-subscriptions-dialog/admin-subscriptions-dialog.component';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit{

  users : UserInfo[] = [];
  constructor(
    private service: PaymentsService,
    private dialog: MatDialog
  ){

  }
  ngOnInit(): void {
    this.service.getAllUsersForAdmin().subscribe({
      next: (result)=>{
        this.users = result;
        this.users = this.users.filter(user => user.webURL !== null);
      }
    })
  }

  openSubscriptions(userInfo: UserInfo){
    let dialogRef = this.dialog.open(AdminSubscriptionsDialogComponent, {
      width: '800px',
      height: '800px',
      data: userInfo,
      panelClass: 'custom-modalbox',
    });
  }

}
