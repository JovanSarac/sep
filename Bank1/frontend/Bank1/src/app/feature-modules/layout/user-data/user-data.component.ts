import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { userDataDto } from '../dto/userDataDto';
import { LayoutService } from '../layout.service';

@Component({
  selector: 'app-user-data',
  templateUrl: './user-data.component.html',
  styleUrls: ['./user-data.component.css']
})
export class UserDataComponent {

  cardForm = new FormGroup({
    PAN: new FormControl('', [Validators.required, Validators.pattern(/^\d{16}$/)]),
    securityCode: new FormControl('', [Validators.required, Validators.pattern(/^\d{3}$/)]),
    cardHolderName: new FormControl('', [Validators.required, Validators.minLength(3)]),
    cardExpirationDate: new FormControl('', Validators.required)
  })

  constructor(private layoutService: LayoutService) {}

  onSubmit() {
    if (this.cardForm.valid) {
      const userData: userDataDto = {
        PAN: parseInt(this.cardForm.value.PAN!, 10), 
        securityCode: parseInt(this.cardForm.value.securityCode!, 10), 
        cardHolderName: this.cardForm.value.cardHolderName!, 
        cardExpirationDate: new Date(this.cardForm.value.cardExpirationDate!) 
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