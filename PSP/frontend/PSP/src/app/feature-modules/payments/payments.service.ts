import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PaymentService } from './model/payment_service.model';
import { environment } from 'src/env/environment';
import { SubscriptionRequest } from './model/subscription-request.model';
import { SubscriptionDto } from './model/subscription.model';
import { UserInfo } from '../layout/model/userinfo';
import { ApiKeyDto } from './model/api-key.model';

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
    return this.http.post<SubscriptionDto>(environment.apiHost + 'user/create_subscription', subscriptionDuration)
  }

  getSubscriptionsForUser(userId: number): Observable<SubscriptionDto[]> {
    return this.http.get<SubscriptionDto[]>(environment.apiHost + 'user_active_subscription/' + userId);
  }

  getSubscriptionsForUserAdmin(userId: number): Observable<SubscriptionDto[]> {
    return this.http.get<SubscriptionDto[]>(environment.apiHost + 'user_subscription/' + userId);
  }

  updateSubscription(subscriptionDTO: SubscriptionDto): Observable<any> {
    return this.http.put<any>(environment.rabbitMQ + 'publishUpdateSubscription', subscriptionDTO);
  }

  /*updateSubscriptionByAdmin(subscriptionDTO: SubscriptionDto): Observable<any> {
    return this.http.put<any>(environment.apiHost + 'user/update_subscription', subscriptionDTO);
  }*/

  getAllUsersForAdmin():Observable<UserInfo[]>{
    return this.http.get<UserInfo[]>(environment.apiHost + 'admin/users');
  }

  saveApiKey(apiKey: ApiKeyDto):Observable<String>{
    return this.http.post<String>('http://localhost:8093/api/apiKey', apiKey, {
      responseType: 'text' as 'json'})
  }
}
