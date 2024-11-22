import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserInfo } from './model/userinfo';
import { environment } from 'src/env/environment';
import { PaymentService } from '../payments/model/payment_service.model';

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
}
