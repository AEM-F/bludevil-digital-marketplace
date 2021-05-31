import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Product} from '../../../../common/product';
import {ProductService} from '../../../../services/product.service';
import {Videogame} from '../../../../common/videogame';
import {AbstractControl, FormControl, FormGroup, Validators} from '@angular/forms';
import {ProductValidators} from '../../../validators/product.validators';
import {ProductPlatform} from '../../../../common/productplatform';
import {Genre} from '../../../../common/genre';
import {ProductPlatformService} from '../../../../services/product-platform.service';
import {GenreService} from '../../../../services/genre.service';
import {ImageService} from '../../../../services/image.service';
import {SoftwareProduct} from '../../../../common/softwareproduct';
import {FirstToUpperPipe} from '../../../../common/pipes/first-to-upper.pipe';
import {animate, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-product-edit',
  templateUrl: './product-edit.component.html',
  styleUrls: ['./product-edit.component.css'],
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
export class ProductEditComponent implements OnInit {

  productId = 0;
  product: Product = null;
  getProductError = null;
  isLoadingProduct = false;
  editForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(255)
    ]),
    active: new FormControl(),
    productPlatform: new FormControl('', [Validators.required]),
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
    ])
  });
  allowedFileTypes = ['image/png', 'image/jpeg', 'image/jpg'];
  fileToUpload: File = null;
  fileUploadMode = 'url';
  productType = '';
  isActive: boolean;
  platforms: ProductPlatform[] = [];
  genres: Genre[] = [];
  selectedGenres: Genre[] = [];
  genreServerError;
  platformServerError;
  errorUploadImage: any;
  errorUpdateProduct: any;

  get name(): AbstractControl{
    return this.editForm.get('name');
  }
  get productPlatform(): AbstractControl{
    return this.editForm.get('productPlatform');
  }
  get price(): AbstractControl{
    return this.editForm.get('price');
  }
  get description(): AbstractControl{
    return this.editForm.get('description');
  }
  get systemRequirements(): AbstractControl{
    return this.editForm.get('systemRequirements');
  }
  get fileUrl(): AbstractControl{
    return this.editForm.get('fileUrl');
  }
  get releaseDate(): AbstractControl{
    return this.editForm.get('releaseDate');
  }

  get active(): AbstractControl{
    return this.editForm.get('active');
  }

  get productAsVideoGame(): Videogame{
    return (this.product as Videogame);
  }

  constructor(private route: ActivatedRoute,
              private router: Router,
              private productService: ProductService,
              private platformService: ProductPlatformService,
              private genreService: GenreService,
              private imageService: ImageService) {}

  ngOnInit(): void {
    this.listPlatforms();
    this.listGenres();
    this.route.params.subscribe(params => {
      const id = +params.id;
      if (id <= 0){
        this.router.navigate(['/not-found']);
      }
      this.productId = id;
      this.getProduct();
    });
  }

  handleVideoGameInfo(): void{
    if (this.product.type === 'videogame'){
      this.editForm.controls.releaseDate.setValidators([
        Validators.required,
        Validators.maxLength(10),
        Validators.pattern(ProductValidators.datePattern)]);
      this.selectedGenres = this.productAsVideoGame.genres;
      console.log(this.selectedGenres);
      this.editForm.controls.releaseDate.patchValue(this.productAsVideoGame.releaseDate);
    }
  }

  getProduct(): void{
    this.isLoadingProduct = true;
    this.productService.getProductById(this.productId).subscribe(
      data => {
        this.isLoadingProduct = false;
        this.product = data;
        this.productType = this.product.type;
        this.isActive = this.product.active;
        const firstToUpperPipe: FirstToUpperPipe = new FirstToUpperPipe();
        this.editForm.controls.name.patchValue(this.product.name);
        this.editForm.controls.productPlatform.patchValue(firstToUpperPipe.transform(this.product.productPlatform.name));
        this.editForm.controls.price.patchValue(this.product.price.toString());
        this.editForm.controls.description.patchValue(this.product.description);
        this.editForm.controls.systemRequirements.patchValue(this.product.systemRequirements);
        this.editForm.controls.fileUrl.patchValue(this.product.imageUrl);
        this.editForm.controls.active.patchValue(this.product.active);
        this.handleVideoGameInfo();
        // setTimeout(() => { this.highlightProductGenres(); }, 500);
        // console.log(data);
    },
      getProductError => {
        this.isLoadingProduct = false;
        this.getProductError = getProductError.error.message;
        console.log(getProductError);
      }
      );
  }

  handleProductError(): void {
    this.getProductError = null;
    this.getProduct();
  }

  handleProductImage(imagePath: string): string{
    return this.productService.handleProductImage(imagePath);
  }

  onProductStateChange(): void{
    this.isActive = this.active.value;
    // console.log(this.active.value);
  }

  displayProductStateMsg(): string{
    if (this.isActive){
      return 'Active';
    }
    return 'Inactive';
  }

  listPlatforms(): void{
    this.platformService.getPlatformList().subscribe(
      data => {
        this.platforms = data;
      },
      error1 => {
        this.platformServerError = error1.error.message;
        console.log(error1);
      }
    );
  }

  listGenres(): void{
    this.genreService.getGenreList('all', 0, 0).subscribe(
      data => {
        this.genres = data;
      },
      error1 => {
        this.genreServerError = error1.error.message;
        console.log(error1);
      }
    );
  }

  updateFileUploadMode(mode: string): void{
    this.fileUploadMode = mode;
    if (this.fileUploadMode === 'url'){
      this.editForm.controls.fileUrl.setValidators([Validators.required, Validators.maxLength(255)]);
      this.editForm.controls.fileUrl.updateValueAndValidity();
    }
    else if (this.fileUploadMode === 'uploadImage'){
      this.editForm.controls.fileUrl.patchValue('');
      this.editForm.controls.fileUrl.setValidators([]);
      this.editForm.controls.fileUrl.updateValueAndValidity();
    }
  }

  toggleGenre(genre: Genre){
    console.log(genre);
    if (this.selectedGenres.length === 0){
      this.selectedGenres.push(genre);
    }
    else{
      const index = this.selectedGenres.map( selectedGenre => selectedGenre.genreName).indexOf(genre.genreName);
      if (index > -1) {
        this.selectedGenres.splice(index, 1);
      }
      else {
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
      const index = this.selectedGenres.map( selectedGenre => selectedGenre.genreName).indexOf(genre.genreName);
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

  validateSelectedGenres(): boolean{
    if (this.productType === 'videogame'){
      if (this.selectedGenres.length === 0){
        return false;
      }
      return true;
    }
    else if (this.productType === 'softwareproduct'){
      return true;
    }
  }

  updateProductRequest(productToSave: Product): void{
    this.productService.updateProduct(productToSave, this.productId).subscribe(
      dataUpdateProduct => {
        const returnedUpdatedProduct: Product = dataUpdateProduct;
        console.log(returnedUpdatedProduct);
      },
      errorUpdateProduct => {
        this.errorUpdateProduct = errorUpdateProduct.error.message || errorUpdateProduct.error;
        this.editForm.setErrors(
          { invalidUpdateProduct: true }
        );
      });
  }

  onEditProductSubmit(): void {
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
          this.active.value,
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
          this.active.value,
          +this.price.value
        );
        break;
      }
    }
    console.log(productToSave);
    if (this.fileUploadMode === 'uploadImage'){
      this.imageService.uploadProductImage(this.fileToUpload, this.productId).subscribe(
        dataUploadImage => {
          productToSave.imageUrl = dataUploadImage.message;
          this.updateProductRequest(productToSave);
          this.navigateToProductManage();
        },
        errorUploadImg => {
          this.errorUploadImage = errorUploadImg.error.message;
          this.editForm.setErrors({ invalidImageUpload: true });
        }
      );
    }
    else{
      this.updateProductRequest(productToSave);
      this.navigateToProductManage();
    }
  }

  handleUpdateError(): void{
    this.errorUpdateProduct = null;
    this.editForm.setErrors({});
  }

  handleUploadError(): void{
    this.errorUploadImage = null;
    this.editForm.setErrors({});
  }
  navigateToProductManage(): void{
    setTimeout(() => {
      this.router.navigate(['admin/products']);
    }, 1000);
  }
}

