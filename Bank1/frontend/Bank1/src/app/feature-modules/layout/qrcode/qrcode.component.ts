import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LayoutService } from '../layout.service';

@Component({
  selector: 'app-qrcode',
  templateUrl: './qrcode.component.html',
  styleUrls: ['./qrcode.component.css']
})
export class QrcodeComponent {

  constructor(private route: ActivatedRoute, private layoutServiceL: LayoutService) {}

  amount: number = 0;
  successUrl: string = '';
  failedUrl: string = '';
  errorUrl: string = '';
  qrData: string = '';
  qrPaymentId: string = '';

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      const amount = params.get('amount');
      const successUrl = params.get('successUrl');
      const failedUrl = params.get('failedUrl');
      const errorUrl = params.get('errorUrl');
      const qrData = params.get('qrData');
      const qrPaymentId = params.get('qrPaymentId');
      localStorage.setItem('access-token', this.route.snapshot.queryParamMap.get('accessToken') || '');
      localStorage.setItem('refresh-token', '');

      this.amount = amount ? Number(amount) : 0;
      this.successUrl = successUrl || '';
      this.failedUrl = failedUrl || '';
      this.errorUrl = errorUrl || '';
      this.qrData = qrData || '';
      this.qrPaymentId = qrPaymentId || '';

      const url = new URL(window.location.href);
      url.searchParams.delete('accessToken');
      url.searchParams.delete('amount');
      url.searchParams.delete('successUrl'); 
      url.searchParams.delete('failedUrl'); 
      url.searchParams.delete('errorUrl'); 
      url.searchParams.delete('qrData'); 
      url.searchParams.delete('qrPaymentId'); 
      window.history.replaceState({}, '', url.toString());
      

      if (this.qrPaymentId) {
        this.checkStatus();  // start polling
      }
    });
  }

  checkStatus(): void {
    this.layoutServiceL.getQRRequestStatus(this.qrPaymentId).subscribe({
      next: (result) => {
        if (result === 'COMPLETED') {
          window.location.href = this.successUrl;
        } else if (result === 'FAILED') {
          window.location.href = this.failedUrl;
        } else if (result === 'ERROR') {
          window.location.href = this.errorUrl;
        } else {
          setTimeout(() => this.checkStatus(), 3000);
        }
      },
      error: () => {
        setTimeout(() => this.checkStatus(), 3000);
      }
    });
  }

}
