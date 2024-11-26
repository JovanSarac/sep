import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { userDataDto } from './dto/userDataDto';
import { Observable } from 'rxjs';
import { environment } from 'src/env/environment';

@Injectable({
  providedIn: 'root'
})
export class LayoutService {

  constructor(private http: HttpClient) { }

  validateData(userDataDto: userDataDto): Observable<String> {
    return this.http.post<String>(environment.apiHost + 'bank1/accounts/validateData', userDataDto, {
      responseType: 'text' as 'json',});
  }
}
