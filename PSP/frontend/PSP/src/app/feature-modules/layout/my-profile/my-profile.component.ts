import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { User } from 'src/app/infrastructure/auth/model/user.model';
import { LayoutService } from '../layout.service';
import { UserInfo } from '../model/userinfo';

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrl: './my-profile.component.css'
})
export class MyProfileComponent implements OnInit{
  user!: User;
  userInfo!: UserInfo;
  selectedTab: string = 'personal'

  constructor(
    private authService: AuthService,
    private layoutService: LayoutService
  ){}

  ngOnInit(): void {
    this.authService.user$.subscribe((user) => {
      this.user = user;
    });

    this.layoutService.getUserInfoById(this.user.id).subscribe({
      next: (result) =>{
        this.userInfo = result;
      }
    })
  }

  changeTab(tab: string){
    this.selectedTab = tab;
  }

  onLogout(): void {
    this.authService.logout();
  }
}
