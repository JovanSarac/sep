import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'xp-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  selectedType: string = 'personal';
  startUrl: string = 'http://localhost:4200/';

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute
  ){

  }
  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      if (params['type'] === 'business') {
        this.selectedType = 'business'; 
      }
    });
  }

  selectType(type: string): void {
    this.selectedType = type;
    if(type == 'business'){
      this.router.navigate([], {
        relativeTo: this.activatedRoute,
        queryParams: { type: 'business' },
        queryParamsHandling: 'merge'
      });
    }else if(type == 'personal'){
      this.router.navigate([], {
        relativeTo: this.activatedRoute,
      });
    }
  }

  goToLogin(){
    this.router.navigate(['/login']);
  }

  goToHome(){
    this.router.navigate([''])
  }
}
