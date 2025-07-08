import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LayoutService } from '../layout.service';
import { PaymentService } from '../../payments/model/payment_service.model';
import { paymentDataDto } from '../dto/paymentDataDto';
import { SessionDto } from '../dto/sessionDto';

@Component({
  selector: 'app-available-services',
  templateUrl: './available-services.component.html',
  styleUrl: './available-services.component.css'
})
export class AvailableServicesComponent implements OnInit {
  sessionId!: string;
  activePspServices: PaymentService[] = [];
  paymentData?: paymentDataDto;

  constructor(
    private route: ActivatedRoute,
    private layoutServiceL: LayoutService
  ) { }

  ngOnInit(): void {
    this.sessionId = this.route.snapshot.paramMap.get('sessionId') || '';
    if (this.sessionId != '') {
      this.layoutServiceL.getSesstionById(Number(this.sessionId)).subscribe({
        next: (result) => {
          this.activePspServices = result;
        }
      })
    }

  }

  choosePaymentMethod(ps: any) {
    switch (ps.id) {
      case -1: {
        alert("bank");

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
      }
        break;
      case -4: {
        this.layoutServiceL.getSessionById(Number(this.sessionId)).subscribe({
          next: (result) => {
            const sessionDto: SessionDto = {
              itemNames: result.itemNames,
              totalPrice: result.totalPrice
            }
            const paymentUrl = new URL("http://localhost:4203/" + this.sessionId);
            paymentUrl.searchParams.append('itemNames', sessionDto.itemNames.join(','))
            paymentUrl.searchParams.append('amount', sessionDto.totalPrice.toString())
            window.location.href = paymentUrl.toString();
          }
        })
      }
        break;
    }
  }
}
