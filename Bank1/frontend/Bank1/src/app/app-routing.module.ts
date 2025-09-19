import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserDataComponent } from './feature-modules/layout/user-data/user-data.component';
import { QrcodeComponent } from './feature-modules/layout/qrcode/qrcode.component';
import { AuthGuard } from '../app/infrastracture/auth/guard/auth.guard';

const routes: Routes = [
  {path: '', component: UserDataComponent, canActivate: [AuthGuard]},
  {path: 'qrCode', component: QrcodeComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
