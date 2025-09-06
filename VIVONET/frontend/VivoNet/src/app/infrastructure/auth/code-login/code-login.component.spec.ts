import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CodeLoginComponent } from './code-login.component';

describe('CodeLoginComponent', () => {
  let component: CodeLoginComponent;
  let fixture: ComponentFixture<CodeLoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CodeLoginComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CodeLoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
