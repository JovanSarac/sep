import { Component, OnInit } from '@angular/core';
import { ServiceOfferingsService } from '../service-offerings.service';
import { ActivatedRoute } from '@angular/router';
import { Service } from '../model/services.model';

@Component({
  selector: 'app-landline',
  templateUrl: './landline.component.html',
  styleUrl: './landline.component.css'
})
export class LandlineComponent implements OnInit{
  constructor(
    private service: ServiceOfferingsService,
    private activatedRoute: ActivatedRoute,
  ){}

  landlineServices : Service[] = [];

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      if (params['type'] === 'business') {
        this.landlineServices = this.service.getLandlineServices();
      }else{
        this.landlineServices = this.service.getLandlineServices();
      }
    });

  }

}
