import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { Registration } from '../model/registration.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {

  samePassword: boolean = true;
  passwordInvalid: boolean = false
  usernameExist: boolean = false;
  errorMessage: string = '';

  registerForm = new FormGroup({
    name: new FormControl('', Validators.required),
    surname: new FormControl('', Validators.required),
    email: new FormControl('', Validators.required),
    companyName: new FormControl('', Validators.required),
    websiteUrl: new FormControl('', Validators.required),
    companyAddress: new FormControl('', Validators.required),
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    repeatPassword: new FormControl('', Validators.required),
  });

  registrationInfo : number = 0;


  constructor(
    private authService: AuthService,
    private router: Router,
    private toast: ToastrService
  ){}

  goToLogin(){
    this.router.navigate(['login'])
  }

  nextPartOfRegistration(){
    this.registrationInfo += 1;
  }

  backPartOfRegistration(){
    this.registrationInfo -= 1;
  }

  register() {
    this.markAllControlsAsTouched();

    if (this.registerForm.invalid) {
      this.toast.error('Please review your entries. Some fields are incorrectly filled or missing.', 'Error');
      return;
    }

    this.samePassword = true;
    this.usernameExist = false;
    this.passwordInvalid = false;
    this.errorMessage = '';
    if (
      this.registerForm.value.password != this.registerForm.value.repeatPassword
    ) {
      this.samePassword = false;
      this.errorMessage = 'The passwords entered must be the same';
      return;
    }

    const registration: Registration = {
      firstName: this.registerForm.value.name || '',
      lastName: this.registerForm.value.surname || '',
      email: this.registerForm.value.email || '',
      companyName: this.registerForm.value.companyName || '',
      websiteUrl: this.registerForm.value.websiteUrl || '',
      companyAddress: this.registerForm.value.companyAddress || '',
      username: this.registerForm.value.username || '',
      password: this.registerForm.value.password || '',
      confirmPassword: this.registerForm.value.repeatPassword || '',
    };

    this.authService.register(registration).subscribe({
      next: (response) => {
        /*const registeredPerson = {
          email: registration.email,
          username: registration.username,
        };
        this.router.navigate(['/check-email'], {
          queryParams: {
            email: registeredPerson.email,
            username: registeredPerson.username,
          },
        });*/
        this.toast.success(response,  'Success!');
        this.router.navigate(['login'])
      },
      error: (err: any) => {   
        console.log(err.error)  
        if (
          err.status === 400 &&
          err.error == 'Username already exists.'
        ) {
          this.usernameExist = true;
          this.toast.error(err.error, 'Error')
          this.errorMessage = "Username already exist!"
        }else if(
          err.status === 400 &&
          err.error == 'Password must be at least 8 characters long, with at least one uppercase letter, one number, and one special character, such as #, !, %, ?, &.'
        
        ){
          this.passwordInvalid = true;
          this.toast.error(err.error, 'Error')
          this.errorMessage = "Password must be at least 8 characters long, with at least one uppercase letter, one number, and one special character, such as #, !, %, ?, &."
        }
      },
    });
   
  }

  private markAllControlsAsTouched(): void {
    Object.values(this.registerForm.controls).forEach((control) => {
      control.markAsTouched();
    });
  }

}
