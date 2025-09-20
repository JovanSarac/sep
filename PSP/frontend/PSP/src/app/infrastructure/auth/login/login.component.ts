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
        if (err.status === 401) {
          this.errorMessage = "Invalid username or password.";
          this.wrongCredential = true;
        } else if (err.status === 423) {
          this.errorMessage = "Your account is locked due to too many failed login attempts. Try again later.";
          this.wrongCredential = true;
        } else {
          this.errorMessage = "Unexpected error occurred.";
          this.wrongCredential = true;
        }
      },
    });

  }

  private markAllControlsAsTouched(): void {
    Object.values(this.loginForm.controls).forEach((control) => {
      control.markAsTouched();
    });
  }

  sendCode(){
    this.wrongCredential = false;
    this.errorMessage = ""
    //this.markUsernameAsTouched();
    const usernameControl = this.loginForm.get('username');

    if (!usernameControl || usernameControl.invalid) {
      // mark username as touched to show validation errors
      usernameControl?.markAsTouched();
      return; // stop form submission
    }

    const login: Login = {
      username: this.loginForm.value.username!,
      password: this.loginForm.value.password!
    };

    this.authService.sendCode(login).subscribe({
      next: () => {
        console.log("nav")
        this.router.navigate(['/code', login.username]);
      },
      error: (err: any) => {
        if (err.status === 401) {
          this.errorMessage = "Invalid username or password or your account is locked.";
        } else {
          this.errorMessage = "Unexpected error occurred.";
          this.wrongCredential = true;
        }
      },
    });
  }
}
