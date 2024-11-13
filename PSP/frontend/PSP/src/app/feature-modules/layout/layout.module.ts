import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './navbar/navbar.component';
import { HomeComponent } from './home/home.component';
import { FooterComponent } from './footer/footer.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/infrastructure/material/material.module';
import { MyProfileComponent } from './my-profile/my-profile.component';



@NgModule({
  declarations: [
    NavbarComponent,
    HomeComponent,
    FooterComponent,
    MyProfileComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    ReactiveFormsModule,
    MaterialModule
  ],
  exports: [
    NavbarComponent,
    FooterComponent
  ],
})
export class LayoutModule { }
