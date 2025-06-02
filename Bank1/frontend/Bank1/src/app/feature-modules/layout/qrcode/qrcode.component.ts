import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-qrcode',
  templateUrl: './qrcode.component.html',
  styleUrls: ['./qrcode.component.css']
})
export class QrcodeComponent {

  constructor(private route: ActivatedRoute) {}

  amount: number = 0;
  successUrl: string = '';
  failedUrl: string = '';
  errorUrl: string = '';
  qrData: string = '';

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      const amount = params.get('amount');
      const successUrl = params.get('successUrl');
      const failedUrl = params.get('failedUrl');
      const errorUrl = params.get('errorUrl');
      const qrData = params.get('qrData');

      this.amount = amount ? Number(amount) : 0;
      this.successUrl = successUrl || '';
      this.failedUrl = failedUrl || '';
      this.errorUrl = errorUrl || '';
      this.qrData = qrData || '';
    });
  }

}
