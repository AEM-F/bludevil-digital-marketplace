import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {UserValidators} from '../validators/user.validators';
import {animate, style, transition, trigger} from '@angular/animations';
import {AuthenticationService} from '../../services/authentication.service';
import {UserDetailsResponse} from '../../common/userdetailsresponse';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css'],
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
    ),
    trigger(
      'OutAnimationFaster',
      [
        transition(
          ':enter',
          [
            style({ opacity: 0 }),
            animate('500ms ease-out',
              style({ opacity: 1 }))
          ]
        )
      ]
    )
  ]
})
export class UserDetailsComponent implements OnInit {
  detailsMode = 'view';
  editDetailsForm: FormGroup;
  userDetails: UserDetailsResponse;
  isLoadingUser = false;
  isUpdatingUser = false;
  userDetailsLoadError;
  userUpdateError;

  constructor(private formBuilder: FormBuilder, private authService: AuthenticationService) { }

  ngOnInit(): void {
    this.editDetailsForm = this.formBuilder.group({
      firstName: new FormControl('', [
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(50),
        UserValidators.cannotContainSpace
      ]),
      lastName: new FormControl('', [
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(50),
        UserValidators.cannotContainSpace
      ]),
      email: new FormControl('', [
        Validators.required,
        UserValidators.emailPatternMatch,
        Validators.maxLength(255)
      ])
    });

    this.loadUserDetails();
  }

  get detailsControls(): {[p: string]: AbstractControl}{
    return this.editDetailsForm.controls;
  }

  loadScript(): void{
    let body = document.body as HTMLDivElement;
    let script = document.createElement('script');
    script.innerHTML = '';
    script.src = '../../assets/js/user-profile-collapse.js';
    script.async = true;
    script.defer = true;
    body.appendChild(script);
  }

  toggleDetailsMode(): void{
    if (this.detailsMode === 'view'){
      this.detailsMode = 'edit';
    }
    else{
      this.detailsMode = 'view';
    }
    if (this.editDetailsForm.touched){
      this.editDetailsForm.reset();
      this.setEditDetailsFormValues();
    }
  }

  onDetailsEditSubmit(){
    this.isUpdatingUser = true;
    if (this.editDetailsForm.invalid){
      this.isUpdatingUser = false;
      return;
    }
    this.authService.updateUserInfo(this.detailsControls.firstName.value,
      this.detailsControls.lastName.value,
      this.detailsControls.email.value).subscribe(
        () => {
        this.isUpdatingUser = false;
        this.detailsMode = 'view';
        this.loadUserDetails();
    },
      error => {
        this.isUpdatingUser = false;
        this.userUpdateError = error.message;
        setTimeout(() => this.userUpdateError = null, 4000);
      });
  }

  setEditDetailsFormValues(): void{
    if (this.userDetails){
      this.detailsControls.firstName.patchValue(this.userDetails.firstName);
      this.detailsControls.lastName.patchValue(this.userDetails.lastName);
      this.detailsControls.email.patchValue(this.userDetails.email);
    }
  }

  loadUserDetails(): void{
    this.isLoadingUser = true;
    this.authService.getUserInfo().subscribe(response => {
      this.isLoadingUser = false;
      response = this.checkUserDetailsLoadResponse(response);
      this.userDetails = response;
      // console.log(response);
      this.setEditDetailsFormValues();
      setTimeout(() => this.loadScript(), 1000);
    },
      error => {
        this.isLoadingUser = false;
        this.userDetailsLoadError = error.message;
        setTimeout(() => this.userDetailsLoadError = null, 4000);
      });
  }

  checkUserDetailsLoadResponse(response: any): any{
    if (response.firstName === null || response.firstName === undefined || response.firstName === ''){
      response.firstName = 'not-provided';
    }
    if (response.lastName === null || response.lastName === undefined || response.lastName === ''){
      response.lastName = 'not-provided';
    }
    if (response.imageUrl === null || response.imageUrl === undefined || response.imageUrl === ''){
      response.imageUrl = '../../../assets/images/default-p-img.png';
    }
    return response;
  }

  getUserRole(): string{
    if (this.userDetails){
      const firstRole = this.userDetails.roles[0];
      if (firstRole === 'MEM'){
        return 'MEMBER';
      }
      else if (firstRole === 'ADM'){
        return 'ADMIN';
      }
    }
    else{
      return '';
    }
  }

}
