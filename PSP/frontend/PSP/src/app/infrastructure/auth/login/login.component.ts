import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Login } from '../model/login.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent{
  wrongCredential: boolean = false;
  errorMessage: string = ""

  loginForm = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  constructor(private authService: AuthService, private router: Router) {}
  

  goToRegistration(){
    this.router.navigate(['registration'])
  }


  login() {
    this.wrongCredential = false;
    this.errorMessage = ""
    this.markAllControlsAsTouched();
    if (this.loginForm.invalid) {
      return;
    }

    const login: Login = {
      username: this.loginForm.value.username!,
      password: this.loginForm.value.password!
    };

    this.authService.login(login).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: (err: any) => {
        
      },
    });

  }

  private markAllControlsAsTouched(): void {
    Object.values(this.loginForm.controls).forEach((control) => {
      control.markAsTouched();
    });
  }
}
