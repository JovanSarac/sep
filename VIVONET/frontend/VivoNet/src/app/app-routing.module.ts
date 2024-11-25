import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './feature-modules/layout/home/home.component';
import { LoginComponent } from './infrastructure/auth/login/login.component';
import { RegistrationComponent } from './infrastructure/auth/registration/registration.component';
import { MobileComponent } from './feature-modules/service-offerings/mobile/mobile.component';
import { LandlineComponent } from './feature-modules/service-offerings/landline/landline.component';
import { InternetComponent } from './feature-modules/service-offerings/internet/internet.component';
import { TelevisionComponent } from './feature-modules/service-offerings/television/television.component';
import { CartComponent } from './feature-modules/layout/cart/cart.component';
import { TransactionStatusComponent } from './feature-modules/layout/transaction-status/transaction-status.component';

const routes: Routes = [
  {path: '', component:  HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'mobile-services', component: MobileComponent},
  {path: 'landline-services', component: LandlineComponent},
  {path: 'internet-services', component: InternetComponent},
  {path: 'television-services', component: TelevisionComponent},
  {path: 'cart', component: CartComponent},
  {path: 'transaction-status/:status', component: TransactionStatusComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
