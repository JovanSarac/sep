import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MobileComponent } from './mobile/mobile.component';
import { LandlineComponent } from './landline/landline.component';
import { InternetComponent } from './internet/internet.component';
import { TelevisionComponent } from './television/television.component';



@NgModule({
  declarations: [
    MobileComponent,
    LandlineComponent,
    InternetComponent,
    TelevisionComponent
  ],
  imports: [
    CommonModule
  ]
})
export class ServiceOfferingsModule { }
