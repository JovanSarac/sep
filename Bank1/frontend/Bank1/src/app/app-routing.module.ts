import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserDataComponent } from './feature-modules/layout/user-data/user-data.component';
import { QrcodeComponent } from './feature-modules/layout/qrcode/qrcode.component';

const routes: Routes = [
  {path: '', component: UserDataComponent},
  {path: 'qrCode', component: QrcodeComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
