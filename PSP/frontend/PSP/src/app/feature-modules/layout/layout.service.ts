import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserInfo } from './model/userinfo';
import { environment } from 'src/env/environment';

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
}
