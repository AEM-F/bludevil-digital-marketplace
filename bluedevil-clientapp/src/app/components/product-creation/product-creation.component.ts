import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormControl, FormGroup, Validators} from '@angular/forms';
import {Genre} from '../../common/genre';
import {ProductPlatform} from '../../common/productplatform';
import {ProductService} from '../../services/product.service';
import {ProductPlatformService} from '../../services/product-platform.service';
import {GenreService} from '../../services/genre.service';
import {ProductValidators} from '../validators/product.validators';
import {Videogame} from '../../common/videogame';
import {SoftwareProduct} from '../../common/softwareproduct';
import {Product} from '../../common/product';
import {ImageService} from '../../services/image.service';
import {Router} from '@angular/router';

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
  allowedFileTypes = ['image/png', 'image/jpeg', 'image/jpg'];
  fileToUpload: File = null;
  fileUploadMode = 'uploadImage';
  productType = 'videogame';
  platforms: ProductPlatform[] = [];
  genres: Genre[] = [];
  selectedGenres: Genre[] = [];
  genreServerError;
  platformServerError;
  productRedirectId: number;
  errorUploadImage: any;
  errorUpdateProduct: any;
  errorCreation: any;

  get type(): AbstractControl{
    return this.creationForm.get('type');
  }
  get name(): AbstractControl{
    return this.creationForm.get('name');
  }
  get productPlatform(): AbstractControl{
    return this.creationForm.get('productPlatform');
  }
  get price(): AbstractControl{
    return this.creationForm.get('price');
  }
  get description(): AbstractControl{
    return this.creationForm.get('description');
  }
  get systemRequirements(): AbstractControl{
    return this.creationForm.get('systemRequirements');
  }
  get fileUrl(): AbstractControl{
    return this.creationForm.get('fileUrl');
  }
  get releaseDate(): AbstractControl{
    return this.creationForm.get('releaseDate');
  }

  constructor(private productService: ProductService,
              private platformService: ProductPlatformService,
              private genreService: GenreService,
              private imageService: ImageService,
              private router: Router) { }

  ngOnInit(): void {
    this.creationForm.patchValue({type: 'videogame'});
    this.listPlatforms();
    this.listGenres();
  }

  listPlatforms(): void{
    this.platformService.getPlatformList().subscribe(
      data => {
        this.platforms = data;
        this.creationForm.controls.productPlatform.patchValue(this.platforms[0].name);
      },
      error1 => {
        this.platformServerError = error1.error.message;
        console.log(error1);
      }
    );
  }

  listGenres(): void{
    this.genreService.getGenreList().subscribe(
      data => {
        this.genres = data;
      },
      error1 => {
        this.genreServerError = error1.error.message;
        console.log(error1);
      }
    );
  }

  updateProductType(type: string): void{
    this.productType = type;
  }

  updateFileUploadMode(mode: string): void{
    this.fileUploadMode = mode;
    if (this.fileUploadMode === 'url'){
      this.creationForm.controls.fileUrl.setValidators([Validators.required, Validators.maxLength(255)]);
      this.creationForm.controls.fileUrl.updateValueAndValidity();
    }
    else if (this.fileUploadMode === 'uploadImage'){
      this.creationForm.controls.fileUrl.patchValue('');
      this.creationForm.controls.fileUrl.setValidators([]);
      this.creationForm.controls.fileUrl.updateValueAndValidity();
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

  validateUploadFile(): boolean{
    // check if file type matched is one of allowed file types and not null
    if ( (this.fileToUpload != null) && (this.allowedFileTypes.indexOf(this.fileToUpload.type) > -1) ){
      return true;
    }
    else{
      return false;
    }
  }

  validateUrlFile(): boolean{
    if (!this.fileUrl.invalid){
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
      return false;
    }
  }

  handleFile(files: FileList): void{
    this.fileToUpload = files.item(0);
    if (!this.validateUploadFile()){
      this.fileToUpload = null;
    }
  }

  createProduct(): void {
    const platformName: string = this.productPlatform.value.toLowerCase();
    const productPlatform: ProductPlatform = this.platforms.find(platform => platform.name === platformName);
    let imageUrl = '';
    if (this.fileUploadMode === 'url'){
      imageUrl = this.fileUrl.value;
    }
    let productToSave: Product;
    switch (this.productType){
      case 'videogame': {
        productToSave = new Videogame(
          this.name.value,
          productPlatform,
          this.description.value,
          imageUrl,
          this.systemRequirements.value,
          true,
          +this.price.value,
          this.releaseDate.value,
          this.selectedGenres
        );
        break;
      }
      case 'softwareproduct': {
        productToSave = new SoftwareProduct(
          this.name.value,
          productPlatform,
          this.description.value,
          imageUrl, this.systemRequirements.value,
          true,
          +this.price.value
        );
        break;
      }
    }
    // console.log(productToSave);
    this.productService.createProduct(productToSave).subscribe(
      data => {
        const returnedProduct: Product = data;
        this.productRedirectId = returnedProduct.id;
        // console.log(returnedProduct);
        if (this.fileUploadMode === `uploadImage`){
          this.imageService.uploadProductImage(this.fileToUpload, returnedProduct.id).subscribe(
            dataUploadImg => {
              const productImagePath: string = dataUploadImg.message;
              returnedProduct.imageUrl = productImagePath;
              this.productService.updateProduct(returnedProduct, returnedProduct.id).subscribe(
                  dataUpdateProduct => {
                    const returnedUpdatedProduct: Product = dataUpdateProduct;
                    console.log(returnedUpdatedProduct);
                  },
                  errorUpdateProduct => {
                    this.errorUpdateProduct = errorUpdateProduct.error.message;
                    this.creationForm.setErrors(
                      { invalidUpdateProduct: errorUpdateProduct.error.message }
                    );
                  }
                );
            },
            errorUploadImg => {
              this.errorUploadImage = errorUploadImg.error.message;
              this.creationForm.setErrors(
                { invalidImageUpload: true }
              );
            }
          );
        }
      },
      errorCreation => {
        this.errorCreation = errorCreation.error.message;
        this.creationForm.setErrors(
          { invalidCreationProduct: true }
        );
      }
    );
  }

  handleCreationError(): void{
    this.errorCreation = null;
    this.creationForm.setErrors({});
  }

  navigateToProductEdit(): void{
    const redirectUrl = `admin/products/edit/${this.productRedirectId}`;
    this.router.navigate([redirectUrl]);
  }
  navigateToProductManage(): void{
    this.router.navigate(['admin/products']);
  }
}
