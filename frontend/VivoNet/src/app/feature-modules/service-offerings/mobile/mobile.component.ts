import { Component, OnInit } from '@angular/core';
import { ServiceOfferingsService } from '../service-offerings.service';
import { Service } from '../model/services.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-mobile',
  templateUrl: './mobile.component.html',
  styleUrl: './mobile.component.css'
})
export class MobileComponent implements OnInit {

  constructor(
    private service: ServiceOfferingsService,
    private activatedRoute: ActivatedRoute,
  ){}

  mobileServices : Service[] = [];

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      if (params['type'] === 'business') {
        this.mobileServices = this.service.getMobileServices();
      }else{
        this.mobileServices = this.service.getMobileServices();
      }
    });

  }

}
