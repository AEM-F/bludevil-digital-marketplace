import { Component, OnInit } from '@angular/core';
import {ProductPlatformService} from '../../../../services/product-platform.service';
import {AbstractControl, FormControl, FormGroup, Validators} from '@angular/forms';
import {ProductValidators} from '../../../validators/product.validators';
import {ProductPlatform} from '../../../../common/productplatform';
import {ActivatedRoute, Params} from '@angular/router';
import {animate, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-product-platform-manage',
  templateUrl: './product-platform-manage.component.html',
  styleUrls: ['./product-platform-manage.component.css'],
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
export class ProductPlatformManageComponent implements OnInit {

  previousKeyword = '';
  currentSearchKeyword = '';
  formOption = '';
  isLoadingPlatforms = false;
  platforms: ProductPlatform[] = [];
  platformFetchError;
  pageNr = 1;
  pageSize = 5;
  totalElements = 0;
  selectedPlatform: ProductPlatform = null;
  editPlatformForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.maxLength(255),
      ProductValidators.cannotContainSpace
    ])
  });
  platformUpdateError;
  platformCreateError;
  isUpdateDone = false;
  createPlatformForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.maxLength(255),
      ProductValidators.cannotContainSpace
    ])
  });
  isCreateDone: boolean;
  get updateFormPlatformName(): AbstractControl{
    return this.editPlatformForm.controls.name;
  }
  get createFormPlatformName(): AbstractControl{
    return this.createPlatformForm.controls.name;
  }

  constructor(private platformService: ProductPlatformService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.listPlatforms();
  }

  onEditFormSubmit(): void{
    this.platformUpdateError = null;
    const platformName: string = this.updateFormPlatformName.value;
    this.platformService.checkNameValidity(platformName).subscribe(
      response => {
        if (!response.valid){
          this.platformUpdateError = 'Update failed, platform name has to be unique';
          setTimeout(() => {
            this.platformUpdateError = null;
          }, 2000);
        }
        else if (response.valid){
          const platformToUpdate: ProductPlatform = new ProductPlatform(platformName.toLowerCase());
          this.platformService.updatePlatform(platformToUpdate, this.selectedPlatform.id).subscribe(
            data => {
              this.selectedPlatform = null;
              this.formOption = '';
              this.listPlatforms();
              this.isUpdateDone = true;
              setTimeout(() => {
                this.isUpdateDone = false;
              }, 2000);
            },
            error2 => {
              this.platformUpdateError = error2.error.message;
            }
          );
        }
      },
      error => {
        this.platformUpdateError = error.error.message;
      }
    );
  }

  onCreateSubmit(): void{
    this.platformCreateError = null;
    const platformName: string = this.createFormPlatformName.value;
    this.platformService.checkNameValidity(platformName).subscribe(
      response => {
        if (!response.valid){
          this.platformCreateError = 'Creation failed, genre name has to be unique';
          setTimeout(() => {
            this.platformCreateError = null;
          }, 2000);
        }
        else if (response.valid){
          console.log('Your genre name is valid');
          const platformToCreate: ProductPlatform = new ProductPlatform(platformName.toLowerCase());
          this.platformService.createPlatform(platformToCreate).subscribe(
            data => {
              this.selectedPlatform = null;
              this.formOption = '';
              this.listPlatforms();
              this.isCreateDone = true;
              setTimeout(() => {
                this.isCreateDone = false;
              }, 2000);
            },
            error2 => {
              this.platformCreateError = error2.error.message;
            }
          );
        }
      },
      error => {
        this.platformCreateError = error.error.message;
      }
    );
  }

  setFormOption(option: string): void{
    if (this.formOption === option){
      this.formOption = '';
    }
    else if (this.formOption !== option || this.formOption === ''){
      if (option === 'create'){
        this.formOption = option;
        this.updateFormPlatformName.patchValue('');
        this.selectedPlatform = null;
        this.platformUpdateError = null;
        this.createFormPlatformName.patchValue('');
      }
      else if (option === 'update'){
        if (this.selectedPlatform === null){
          this.formOption = '';
          this.platformUpdateError = 'No genre selected';
          setTimeout(() => {
            this.platformUpdateError = null;
          }, 2000);
        }
        else{
          this.platformUpdateError = null;
          this.formOption = option;
          this.updateFormPlatformName.patchValue(this.selectedPlatform.name);
        }
      }
    }
  }

  listPlatforms(): void{
    this.route.queryParams.subscribe((params: Params) => {
      if (params.hasOwnProperty('platformName') && params['platformName'] !== ''){
        this.currentSearchKeyword = params['platformName'];
        this.handleSearchPlatforms();
      }
      else{
        if (this.currentSearchKeyword !== ''){
          this.pageNr = 1;
          this.currentSearchKeyword = '';
        }
        this.handleListPlatforms();
      }
    });
  }

  handleListPlatforms(): void{
    this.isLoadingPlatforms = true;
    this.platformService.getPlatformListPaginate(this.pageNr, this.pageSize).subscribe(
      this.processPlatformsFetchResults(),
      this.processPlatformsFetchError()
    );
  }

  handleSearchPlatforms(): void{
    this.isLoadingPlatforms = true;
    if (this.previousKeyword !== this.currentSearchKeyword){
      this.pageNr = 1;
    }
    this.previousKeyword = this.currentSearchKeyword;
    this.platformService.searchPlatformsPaginate(this.pageNr, this.pageSize, this.currentSearchKeyword).subscribe(
      this.processPlatformsFetchResults(),
      this.processPlatformsFetchError()
    );
  }

  processPlatformsFetchResults(){
    return data => {
      this.isLoadingPlatforms = false;
      this.platforms = data.objectsList;
      this.pageNr = data.pageNumber;
      this.pageSize = data.pageSize;
      this.totalElements = data.totalElements;
    };
  }

  processPlatformsFetchError(){
    return error => {
      this.isLoadingPlatforms = false;
      this.pageNr = 1;
      this.isLoadingPlatforms = false;
      this.platformFetchError = error.error.message;
      this.platforms = [];
    };
  }

  checkIfPlatformIsSelected(name: string): boolean{
    if (this.selectedPlatform === null){
      return false;
    }
    else{
      if (this.selectedPlatform.name === name){
        return true;
      }
      else {
        return false;
      }
    }
  }

  togglePlatform(platform: ProductPlatform): void{
    if ( this.selectedPlatform === null){
      this.selectedPlatform = platform;
      this.updateFormPlatformName.patchValue(this.selectedPlatform.name);
    }
    else if (this.selectedPlatform.name === platform.name){
      this.selectedPlatform = null;
    }
    else{
      this.selectedPlatform = platform;
      this.updateFormPlatformName.patchValue(this.selectedPlatform.name);
    }
  }

  updatePageSize(size: number): void{
    this.pageSize = size;
    this.pageNr = 1;
    this.listPlatforms();
  }
}
