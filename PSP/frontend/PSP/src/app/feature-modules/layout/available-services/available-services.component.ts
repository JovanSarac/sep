import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LayoutService } from '../layout.service';
import { PaymentService } from '../../payments/model/payment_service.model';
import { paymentDataDto } from '../dto/paymentDataDto';
import { paymentQRDataDto } from '../dto/paymentQRDataDto';

@Component({
  selector: 'app-available-services',
  templateUrl: './available-services.component.html',
  styleUrl: './available-services.component.css'
})
export class AvailableServicesComponent implements OnInit {
  sessionId!: string;
  activePspServices: PaymentService [] = [];
  paymentData?: paymentDataDto;
  paymentQRData?: paymentQRDataDto;

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
    console.log(ps)
    if (ps.id == -1) {
      this.layoutServiceL.sendRequestToBank1(Number(this.sessionId)).subscribe({
        next: (result) => {
          this.paymentData = result;
          const paymentUrl = new URL(this.paymentData.paymentUrl);
          paymentUrl.searchParams.append('amount', this.paymentData.amount.toString());
          paymentUrl.searchParams.append('successUrl', this.paymentData.successUrl);
          paymentUrl.searchParams.append('failedUrl', this.paymentData.failedUrl);
          paymentUrl.searchParams.append('errorUrl', this.paymentData.errorUrl);
          window.location.href = paymentUrl.toString();
          console.log("zahtev za banku uspesno prosledjen")
        }
      })
    } else if (ps.id == -2) {
      this.layoutServiceL.sendRequestToBank1QRCode(Number(this.sessionId)).subscribe({
        next: (result) => {
          this.paymentQRData = result;
          const paymentUrl = new URL(this.paymentQRData.paymentUrl);
          paymentUrl.searchParams.append('amount', this.paymentQRData.amount.toString());
          paymentUrl.searchParams.append('successUrl', this.paymentQRData.successUrl);
          paymentUrl.searchParams.append('failedUrl', this.paymentQRData.failedUrl);
          paymentUrl.searchParams.append('errorUrl', this.paymentQRData.errorUrl);
          paymentUrl.searchParams.append('qrData', this.paymentQRData.qrData);
          window.location.href = paymentUrl.toString();
          console.log("zahtev za banku uspesno prosledjen")
        }
      })
    }
    
  }
}
