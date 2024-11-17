import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/infrastructure/material/material.module';
import { ServicesComponent } from './services/services.component';
import { SubscriptionDialogComponent } from './subscription-dialog/subscription-dialog.component';
import { ExtendSubscriptionDialogComponent } from './extend-subscription-dialog/extend-subscription-dialog.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { AdminSubscriptionsDialogComponent } from './admin-subscriptions-dialog/admin-subscriptions-dialog.component';



@NgModule({
  declarations: [
    ServicesComponent,
    SubscriptionDialogComponent,
    ExtendSubscriptionDialogComponent,
    AdminDashboardComponent,
    AdminSubscriptionsDialogComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    ReactiveFormsModule,
    MaterialModule
  ],
  exports: [
    ServicesComponent,
    SubscriptionDialogComponent,
    ExtendSubscriptionDialogComponent,
    AdminDashboardComponent,
    AdminSubscriptionsDialogComponent
  ]
})
export class PaymentsModule { }
