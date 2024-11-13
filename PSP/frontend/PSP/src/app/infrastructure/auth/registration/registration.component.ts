import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

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
    private router: Router
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

}
