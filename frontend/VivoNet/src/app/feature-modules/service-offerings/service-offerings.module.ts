import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MobileComponent } from './mobile/mobile.component';
import { LandlineComponent } from './landline/landline.component';
import { InternetComponent } from './internet/internet.component';
import { TelevisionComponent } from './television/television.component';
import { MaterialModule } from 'src/app/infrastructure/material/material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from 'src/app/shared/shared.module';



@NgModule({
  declarations: [
    MobileComponent,
    LandlineComponent,
    InternetComponent,
    TelevisionComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    SharedModule
  ],
  exports:[
    MobileComponent,
    LandlineComponent,
    InternetComponent,
    TelevisionComponent
  ]
})
export class ServiceOfferingsModule { }
