import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {animate, style, transition, trigger} from '@angular/animations';
import {ImageService} from '../../services/image.service';
import {AuthenticationService} from '../../services/authentication.service';

@Component({
  selector: 'app-profile-image',
  templateUrl: './profile-image.component.html',
  styleUrls: ['./profile-image.component.css'],
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
export class ProfileImageComponent implements OnInit {
  fileForm: FormGroup;
  fileToUpload: File = null;
  fileUploadMode = 'url';
  allowedFileTypes = ['image/png', 'image/jpeg', 'image/jpg'];
  uploadMessage;
  errorUpdateImage;
  constructor(private formBuilder: FormBuilder, private imageService: ImageService, private authService: AuthenticationService) {
    this.fileForm = this.formBuilder.group({
      fileUrl : new FormControl('', [Validators.required, Validators.maxLength(255)])
    });
  }

  ngOnInit(): void {
  }

  get fileControls(): {[p: string]: AbstractControl}{
    return this.fileForm.controls;
  }

  get fileUrl(): AbstractControl{
    return this.fileForm.get('fileUrl');
  }

  updateFileUploadMode(mode: string): void{
    this.fileUploadMode = mode;
    if (this.fileUploadMode === 'url'){
      this.fileForm.controls.fileUrl.setValidators([Validators.required, Validators.maxLength(255)]);
      this.fileForm.controls.fileUrl.updateValueAndValidity();
    }
    else if (this.fileUploadMode === 'uploadImage'){
      this.fileForm.controls.fileUrl.patchValue('');
      this.fileForm.controls.fileUrl.setValidators([]);
      this.fileForm.controls.fileUrl.updateValueAndValidity();
    }
  }

  validateUploadFile(): boolean{
    // check if file type matched is one of allowed file types and not null
    if ( (this.fileToUpload != null) && (this.allowedFileTypes.indexOf(this.fileToUpload.type) > -1) ){
      return false;
    }
    else{
      return true;
    }
  }

  validateUrlFile(): boolean{
    if (this.fileForm.invalid){
      return true;
    }
    else{
      return false;
    }
  }

  validateFile(): boolean{
    if (this.fileUploadMode === 'uploadImage'){
      return this.validateUploadFile();
    }
    else if (this.fileUploadMode === 'url'){
      return this.validateUrlFile();
    }
    else {
      return true;
    }
  }

  handleFile(files: FileList): void{
    this.fileToUpload = files.item(0);
    if (this.validateUploadFile()){
      this.fileToUpload = null;
    }
  }

  onFileFormSubmit(){
    if (this.fileUploadMode === 'uploadImage'){
      this.imageService.uploadUserImage(this.fileToUpload).subscribe(
        dataUploadImage => {
          this.uploadMessage = dataUploadImage.message;
          setTimeout(() => this.uploadMessage = null, 2500);
        },
        errorUploadImg => {
          this.fileForm.setErrors({ invalidImageUpload: true });
          this.uploadMessage = errorUploadImg.error.message;
          setTimeout(() => this.handleUploadError(), 2500);
        }
      );
    }
    else{
      this.imageService.updateUserImageUrl(this.fileControls.fileUrl.value).subscribe(dataUpdateImage =>{
        this.uploadMessage = dataUpdateImage.message;
        setTimeout(() => this.uploadMessage = null, 2500);
      },
        error => {
        this.errorUpdateImage = error.message;
          setTimeout(() => this.errorUpdateImage = null, 2500);
        });
    }
  }

  handleUploadError(): void{
    this.uploadMessage = null;
    this.fileForm.setErrors({});
  }

}
