import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Genre} from '../../common/genre';
import {ProductPlatform} from '../../common/productplatform';
import {ProductService} from '../../services/product.service';
import {ProductPlatformService} from '../../services/product-platform.service';
import {GenreService} from '../../services/genre.service';
import {ProductValidators} from '../validators/product.validators';

@Component({
  selector: 'app-product-creation',
  templateUrl: './product-creation.component.html',
  styleUrls: ['./product-creation.component.css']
})
export class ProductCreationComponent implements OnInit {

  creationForm = new FormGroup({
    type: new FormControl('', []),
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(255)
    ]),
    productPlatform: new FormControl('', []),
    price: new FormControl('', [
      Validators.required,
      ProductValidators.cannotContainSpace,
      Validators.pattern(ProductValidators.positiveNumberPattern)
    ]),
    description: new FormControl('', [
      Validators.required,
      Validators.maxLength(2000)
    ]),
    systemRequirements: new FormControl('', [
      Validators.required,
      Validators.maxLength(1000)
    ]),
    fileUrl: new FormControl('', []),
    releaseDate: new FormControl('', [
      Validators.required,
      Validators.maxLength(10),
      Validators.pattern(ProductValidators.datePattern)
    ])
  });
  fileToUpload: File = null;
  fileUploadMode: string = 'upload';
  productType: string = 'videogame';
  platforms: ProductPlatform[] = [];
  genres: Genre[] = [];
  selectedGenres: Genre[] = [];
  error;

  get type(){
    return this.creationForm.get('type');
  }
  get name(){
    return this.creationForm.get('name');
  }
  get productPlatform(){
    return this.creationForm.get('productPlatform');
  }
  get price(){
    return this.creationForm.get('price');
  }
  get description(){
    return this.creationForm.get('description');
  }
  get systemRequirements(){
    return this.creationForm.get('systemRequirements');
  }
  get fileUrl(){
    return this.creationForm.get('fileUrl');
  }
  get releaseDate(){
    return this.creationForm.get('releaseDate');
  }

  constructor(private productService: ProductService,
              private platformService: ProductPlatformService,
              private genreService: GenreService) { }

  ngOnInit(): void {
    this.creationForm.patchValue({type: 'videogame'});
    this.listPlatforms();
    this.listGenres();
  }

  listPlatforms():void{
    this.platformService.getPlatformList().subscribe(
      data => {
        this.platforms = data;
      },
      error1 => {
        this.error = error1.error.message;
        console.log(this.error);
      }
    );
  }

  listGenres():void{
    this.genreService.getGenreList().subscribe(
      data => {
        this.genres = data;
      },
      error1 => {
        this.error = error1.error.message;
        console.log(this.error);
      }
    );
  }

  updateProductType(type: string): void{
    this.productType = type;
  }

  updateFileUploadMode(mode: string): void{
    this.fileUploadMode = mode;
    if (this.fileUploadMode === 'url'){
      this.creationForm.controls['fileUrl'].setValidators([Validators.required, Validators.maxLength(255)]);
      this.creationForm.controls['fileUrl'].updateValueAndValidity();
    }
    else if (this.fileUploadMode === 'upload'){
      this.creationForm.controls['fileUrl'].patchValue('');
      this.creationForm.controls['fileUrl'].setValidators([]);
      this.creationForm.controls['fileUrl'].updateValueAndValidity();
    }
  }

  toggleGenre(genre: Genre){
    console.log(genre);
    if (this.selectedGenres.length === 0){
      this.selectedGenres.push(genre);
    }
    else{
      const index = this.selectedGenres.indexOf(genre, 0);
      if (index > -1) {
        this.selectedGenres.splice(index, 1);
      }
      else{
        this.selectedGenres.push(genre);
      }
    }
    console.log(this.selectedGenres);
  }

  checkIfGenreIsActive(genre: Genre): boolean{
    if (this.selectedGenres.length === 0){
      return false;
    }
    else{
      const index = this.selectedGenres.indexOf(genre, 0);
      if (index > -1) {
        return true;
      }
      else{
        return false;
      }
    }
  }

}
