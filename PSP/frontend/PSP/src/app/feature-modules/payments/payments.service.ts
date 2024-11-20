import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PaymentService } from './model/payment_service.model';
import { environment } from 'src/env/environment';
import { SubscriptionRequest } from './model/subscription-request.model';
import { SubscriptionDto } from './model/subscription.model';
import { UserInfo } from '../layout/model/userinfo';

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

  createSubscription(subscriptionDuration: SubscriptionRequest) : Observable<SubscriptionDto>{
    return this.http.post<SubscriptionDto>(environment.rabbitMQ + 'publishCreateSubscription', subscriptionDuration)
  }

  getSubscriptionsForUser(userId: number): Observable<SubscriptionDto[]> {
    return this.http.get<SubscriptionDto[]>(environment.apiHost + 'user_active_subscription/' + userId);
  }

  updateSubscription(subscriptionDTO: SubscriptionDto): Observable<any> {
    return this.http.put<any>(environment.rabbitMQ + 'publishUpdateSubscription', subscriptionDTO);
  }

  getAllUsersForAdmin():Observable<UserInfo[]>{
    return this.http.get<UserInfo[]>(environment.apiHost + 'admin/users');
  }
  
}
