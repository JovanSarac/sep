import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { userDataDto } from '../dto/userDataDto';
import { LayoutService } from '../layout.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-user-data',
  templateUrl: './user-data.component.html',
  styleUrls: ['./user-data.component.css']
})
export class UserDataComponent implements OnInit {

  cardForm = new FormGroup({
    PAN: new FormControl('', [Validators.required, Validators.pattern(/^\d{16}$/)]),
    securityCode: new FormControl('', [Validators.required, Validators.pattern(/^\d{3}$/)]),
    cardHolderName: new FormControl('', [Validators.required, Validators.minLength(3)]),
    cardExpirationDate: new FormControl('', Validators.required)
  })

  constructor(private layoutService: LayoutService, private route: ActivatedRoute) {}

  receivedData: string | null = null;
  amount: number = 0;
  successUrl: string = '';
  failedUrl: string = '';
  errorUrl: string = '';

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      const amount = params.get('amount');
      const successUrl = params.get('successUrl');
      const failedUrl = params.get('failedUrl');
      const errorUrl = params.get('errorUrl');

      this.amount = amount ? Number(amount) : 0;
      this.successUrl = successUrl || '';
      this.failedUrl = failedUrl || '';
      this.errorUrl = errorUrl || '';

      console.log('Amount:', amount);
      console.log('successUrl:', successUrl);
      console.log('failedUrl:', failedUrl);
      console.log('errorUrl:', errorUrl);
    });
  }

  onSubmit() {
    if (this.cardForm.valid) {
      const userData: userDataDto = {
        PAN: parseInt(this.cardForm.value.PAN!, 10), 
        securityCode: parseInt(this.cardForm.value.securityCode!, 10), 
        cardHolderName: this.cardForm.value.cardHolderName!, 
        cardExpirationDate: new Date(this.cardForm.value.cardExpirationDate!) ,
        amount: this.amount
      };
      this.layoutService.validateData(userData).subscribe({
        next: (result) => {
          if (result.valueOf() === "uspesno") {
            window.location.href = this.successUrl;
          }
          if (result === "pogresno") {
            window.location.href = this.errorUrl;
          }
          if (result === "neuspesno") {
            window.location.href = this.failedUrl;
          }
          window.location.href = this.errorUrl
          console.log('Form data:', this.cardForm.value);
          console.log(result);
        }
      })
    } else {
      console.error('Form is invalid');
    }
  }

}