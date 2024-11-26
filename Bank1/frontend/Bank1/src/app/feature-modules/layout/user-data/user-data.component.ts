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

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      const amount = params.get('amount');

      this.amount = amount ? Number(amount) : 0;

      console.log('Amount:', amount);
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
          console.log('Form data:', this.cardForm.value);
          console.log(result);
        }
      })
    } else {
      console.error('Form is invalid');
    }
  }

}