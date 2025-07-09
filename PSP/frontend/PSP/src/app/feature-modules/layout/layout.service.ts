import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserInfo } from './model/userinfo';
import { environment } from 'src/env/environment';
import { PaymentService } from '../payments/model/payment_service.model';
import { paymentDataDto } from './dto/paymentDataDto';
import { paymentQRDataDto } from './dto/paymentQRDataDto';
import { SessionDto } from './dto/sessionDto';

@Injectable({
  providedIn: 'root'
})
export class LayoutService {

  constructor(
    private http: HttpClient
  ) { }

  getUserInfoById(id: number): Observable<UserInfo> {
    return this.http.get<UserInfo>(environment.apiHost + 'user/' + id);
  }

  getSesstionById(id:number): Observable<PaymentService[]>{
    return this.http.get<PaymentService[]>(environment.apiHost + 'active_pspservices_bysession/' + id);
  }

  sendRequestToBank1(sessionId: number): Observable<paymentDataDto>{
    //return this.http.get<String>(environment.rabbitMQ + 'publishSendRequest/' + sessionId);
    return this.http.get<paymentDataDto>(environment.apiHost+ 'psp/requests/sendRequest/' + sessionId);
  } //treba dodati da se salje i odabrani nacin placanja

  sendRequestToBank1QRCode(sessionId: number): Observable<paymentQRDataDto>{
    //return this.http.get<String>(environment.rabbitMQ + 'publishSendRequest/' + sessionId);
    return this.http.get<paymentQRDataDto>(environment.apiHost+ 'psp/requests/sendRequestQRCode/' + sessionId);
  }

  getSessionById(id: number) : Observable<SessionDto>{
    return this.http.get<SessionDto>(environment.apiHost + 'session/' + id);
  }
}
