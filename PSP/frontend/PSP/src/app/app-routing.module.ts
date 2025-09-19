import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './feature-modules/layout/home/home.component';
import { LoginComponent } from './infrastructure/auth/login/login.component';
import { RegistrationComponent } from './infrastructure/auth/registration/registration.component';
import { MyProfileComponent } from './feature-modules/layout/my-profile/my-profile.component';
import { ServicesComponent } from './feature-modules/payments/services/services.component';
import { AdminDashboardComponent } from './feature-modules/payments/admin-dashboard/admin-dashboard.component';
import { AvailableServicesComponent } from './feature-modules/layout/available-services/available-services.component';
import { CodeLoginComponent } from './infrastructure/auth/code-login/code-login.component';
import { AuthGuard } from './infrastructure/auth/guard/auth.guard';

const routes: Routes = [
  {path: '', component:  HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'code/:username', component: CodeLoginComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'my-profile', component: MyProfileComponent, canActivate:[AuthGuard]},
  {path: 'services', component: ServicesComponent, canActivate:[AuthGuard]},
  {path: 'admin-dashboard', component: AdminDashboardComponent, canActivate:[AuthGuard]},
  {path: 'available-service/:sessionId', component: AvailableServicesComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
