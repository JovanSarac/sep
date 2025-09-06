import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Login } from '../model/login.model';

@Component({
  selector: 'app-code-login',
  templateUrl: './code-login.component.html',
  styleUrl: './code-login.component.css'
})
export class CodeLoginComponent {
  wrongCredential: boolean = false;
  errorMessage: string = ""

  loginForm = new FormGroup({
    code: new FormControl('', Validators.required),
  });

  constructor(private authService: AuthService, private router: Router) {}

 private markAllControlsAsTouched(): void {
    Object.values(this.loginForm.controls).forEach((control) => {
      control.markAsTouched();
    });
  }

  login() {
    this.wrongCredential = false;
    this.errorMessage = ""
    this.markAllControlsAsTouched();
    if (this.loginForm.invalid) {
      return;
    }

    const login: Login = {
      username: "VivoNet"!,
      password: this.loginForm.value.code!
    };

    this.authService.codeLogin(login).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: (err: any) => {
        if (err.status === 401) {
          this.errorMessage = "Invalid code.";
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
}