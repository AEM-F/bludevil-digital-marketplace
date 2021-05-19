import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../../services/authentication.service';
import {first} from 'rxjs/operators';
import {UserService} from '../../services/user.service';
import {animate, style, transition, trigger} from '@angular/animations';
import {DOCUMENT} from '@angular/common';
import {UserValidators} from '../validators/user.validators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  animations: [
    trigger(
      'inOutAnimation',
      [
        transition(
          ':enter',
          [
            style({ opacity: 0 }),
            animate('1s ease-out',
              style({ opacity: 1 }))
          ]
        ),
        transition(
          ':leave',
          [
            style({ opacity: 1 }),
            animate('1s ease-in',
              style({ opacity: 0 }))
          ]
        )
      ]
    )
  ]
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  loading = false;
  loadingSignUp = false;
  submitted = false;
  error = '';
  isPasswordHidden = true;
  signUpForm: FormGroup;
  arePasswordHiddenSignUp = true;
  selectedForm = 'login';

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private userService: UserService,
    @Inject(DOCUMENT) private document: Document
  ) {
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', [
        Validators.required,
        UserValidators.emailPatternMatch,
        Validators.maxLength(255)
      ]],
      password: ['', [
        Validators.required,
        UserValidators.cannotContainSpace,
        Validators.maxLength(255)
      ]]
    });

    this.signUpForm = this.formBuilder.group({
      username: ['', [
        Validators.required,
        UserValidators.emailPatternMatch,
        Validators.maxLength(255)
      ]],
      password: ['', [
        Validators.required,
        UserValidators.cannotContainSpace,
        Validators.minLength(4),
        Validators.maxLength(255)
      ]],
      confirmPassword: ['', [
        Validators.required,
        UserValidators.cannotContainSpace,
        Validators.minLength(4),
        Validators.maxLength(255)
      ]]
    }, { validators: [UserValidators.passwordsValuesCompare]});
  }

  // convenience getter for easy access to form fields
  get getLoginForm() { return this.loginForm.controls; }
  get getSignupForm() {return this.signUpForm.controls; }

  private handleLoginRedirect(): void {
    const roles: string[] = this.authenticationService.getUserRoles;
    if (roles.indexOf('ADM') > -1){
      this.router.navigate(['/admin/products']);
    }
    else if (roles.indexOf('MEM') > -1){
      this.router.navigate(['/products']);
    }
  }

  private handleLoginError(error): void {
    if (error.status === 401){
      this.error = 'Login failed! The given email or password are invalid';
    }
    else{
      this.error = error.message;
    }
    this.loading = false;
    setTimeout(() => this.error = '', 4000);
  }

  onFormSwitch(formType: string): void{
    this.selectedForm = formType;
  }

  checkActiveFormInvalid(): boolean{
    if (this.selectedForm === 'login' && this.loginForm.invalid){
      return true;
    }
    else if (this.selectedForm === 'signup' && this.signUpForm.invalid){
      return true;
    }
    else{
      return false;
    }
  }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.authenticationService.login(this.getLoginForm.username.value, this.getLoginForm.password.value)
      .pipe(first())
      .subscribe({
        next: () => {
          this.handleLoginRedirect();
        },
        error: error => {
          this.handleLoginError(error);
        }
      });
  }

  onPasswordBtnClick(): void{
    const passwordField = this.document.getElementById('password');
    this.isPasswordHidden = !this.isPasswordHidden;
    if (this.isPasswordHidden){
      passwordField.setAttribute('type', 'password');
    }
    else{
      passwordField.setAttribute('type', 'text');
    }
  }

  onSignUpPasswordBtnClick(): void{
    const passField = this.document.getElementById('su-password');
    const confirmPassField = this.document.getElementById('su-confirmPassword');
    this.arePasswordHiddenSignUp = !this.arePasswordHiddenSignUp;
    if (this.arePasswordHiddenSignUp){
      passField.setAttribute('type', 'password');
      confirmPassField.setAttribute('type', 'password');
    }
    else{
      passField.setAttribute('type', 'text');
      confirmPassField.setAttribute('type', 'text');
    }
  }

  private handleRegisterError(error): void {
    if (error.hasOwnProperty('error')){
      this.error = error.error.message;
    }
    else{
      this.error = error.message;
    }
    this.loadingSignUp = false;
    setTimeout(() => this.error = '', 4000);
  }

  onRegisterSubmit(){
    if (this.signUpForm.invalid){
      return;
    }

    this.loadingSignUp = true;
    this.authenticationService.signUp(this.getSignupForm.username.value , this.getSignupForm.password.value).subscribe(next => {
      this.authenticationService.login(this.getSignupForm.username.value, this.getSignupForm.password.value).pipe(first())
        .subscribe({
          next: () => {
            this.handleLoginRedirect();
          },
          error: error => {
            this.handleLoginError(error);
          }
        });
    }, error => {
      this.handleRegisterError(error);
    });
  }
}
