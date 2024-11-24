import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LayoutService } from '../layout.service';
import { PaymentService } from '../../payments/model/payment_service.model';

@Component({
  selector: 'app-available-services',
  templateUrl: './available-services.component.html',
  styleUrl: './available-services.component.css'
})
export class AvailableServicesComponent implements OnInit {
  sessionId!: string;
  activePspServices: PaymentService [] = [];

  constructor(
    private route: ActivatedRoute,
    private layoutServiceL: LayoutService
  ) {}

  ngOnInit(): void {
    this.sessionId = this.route.snapshot.paramMap.get('sessionId') || '';
    if(this.sessionId != ''){
      this.layoutServiceL.getSesstionById(Number(this.sessionId)).subscribe({
        next: (result)=>{
          this.activePspServices = result;
        }
      })
    }
  
  }

  choosePaymentMethod(ps: any) {
    this.layoutServiceL.sendRequestToBank1(Number(this.sessionId)).subscribe({
      next: (result) => {
        console.log("zahtev za banku uspesno prosledjen")
      }
    })
  }
}
