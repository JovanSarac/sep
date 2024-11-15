import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PaymentService } from './model/payment_service.model';
import { environment } from 'src/env/environment';

@Injectable({
  providedIn: 'root'
})
export class PaymentsService {

  constructor(
    private http: HttpClient
  ) { }

  getActivePaymentServices(): Observable<PaymentService[]> {
    return this.http.get<PaymentService[]>(environment.apiHost + 'user/active_payment_services');
  }
  
}
