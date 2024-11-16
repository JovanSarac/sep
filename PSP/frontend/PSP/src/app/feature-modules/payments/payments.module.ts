import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/infrastructure/material/material.module';
import { ServicesComponent } from './services/services.component';
import { SubscriptionDialogComponent } from './subscription-dialog/subscription-dialog.component';



@NgModule({
  declarations: [
    ServicesComponent,
    SubscriptionDialogComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    ReactiveFormsModule,
    MaterialModule
  ],
  exports: [
    ServicesComponent,
    SubscriptionDialogComponent
  ]
})
export class PaymentsModule { }
