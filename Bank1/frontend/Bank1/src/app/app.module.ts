import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ReactiveFormsModule } from '@angular/forms';
import { LayoutModule } from './feature-modules/layout/layout.module';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { QRCodeModule } from 'angularx-qrcode';
import { initKeycloak } from './infrastracture/auth/init/keycloak-init.factory';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { AuthInterceptor } from './infrastracture/auth/jwt/jwt.interceptor';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    LayoutModule,
    QRCodeModule,
    KeycloakAngularModule,
  ],
  providers: [{
            provide: APP_INITIALIZER,
            useFactory: initKeycloak,
            multi: true,
            deps: [KeycloakService],
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true,
        },],
  bootstrap: [AppComponent]
})
export class AppModule { }
