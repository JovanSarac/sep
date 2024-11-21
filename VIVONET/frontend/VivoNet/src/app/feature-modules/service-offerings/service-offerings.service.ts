import { Injectable } from '@angular/core';
import { Service, TypeService, TypeUser } from './model/services.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/env/environment';

@Injectable({
  providedIn: 'root'
})
export class ServiceOfferingsService {

  constructor(private http: HttpClient) { }

  getMobileServicesBusiness(): Observable<Service[]> {
    return this.http.get<Service[]>(environment.apiHost + 'vivonetServices/mobile/business');
  }

  getLandlineServicesBusiness(): Observable<Service[]> {
    return this.http.get<Service[]>(environment.apiHost + 'vivonetServices/landline/business');
  }

  getInternetServicesBusiness(): Observable<Service[]> {
    return this.http.get<Service[]>(environment.apiHost + 'vivonetServices/internet/business');
  }

  getTelevisionServicesBusiness(): Observable<Service[]> {
    return this.http.get<Service[]>(environment.apiHost + 'vivonetServices/tv/business');
  }

  getMobileServicesPersonal(): Observable<Service[]> {
    return this.http.get<Service[]>(environment.apiHost + 'vivonetServices/mobile/personal');
  }

  getLandlineServicesPersonal(): Observable<Service[]> {
    return this.http.get<Service[]>(environment.apiHost + 'vivonetServices/landline/personal');
  }

  getInternetServicesPersonal(): Observable<Service[]> {
    return this.http.get<Service[]>(environment.apiHost + 'vivonetServices/internet/personal');
  }

  getTelevisionServicesPersonal(): Observable<Service[]> {
    return this.http.get<Service[]>(environment.apiHost + 'vivonetServices/tv/personal');
  }

}
