import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LayoutModule } from './feature-modules/layout/layout.module';
import { MaterialModule } from './infrastructure/material/material.module';
import { AuthModule } from './infrastructure/auth/auth.module';
import { SharedModule } from './shared/shared.module';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { JwtInterceptor } from './infrastructure/auth/jwt/jwt.interceptor';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { PaymentsModule } from './feature-modules/payments/payments.module';
import { DatePipe } from '@angular/common';

@NgModule({ declarations: [
        AppComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    bootstrap: [AppComponent],
    imports: [BrowserModule,
        AppRoutingModule,
        LayoutModule,
        PaymentsModule,
        MaterialModule,
        AuthModule,
        SharedModule,
        BrowserAnimationsModule,
        ToastrModule.forRoot({
            timeOut: 4000,
            extendedTimeOut: 1000,
            maxOpened: 3,
            positionClass: 'toast-bottom-right',
            progressBar: true,
            progressAnimation: 'increasing',
        })], 
    providers: [
        DatePipe,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: JwtInterceptor,
            multi: true,
        },
        provideHttpClient(withInterceptorsFromDi()),
    ] })
export class AppModule { }
