import {Component, OnInit} from '@angular/core';
import {Product} from '../../../../common/product';
import {ProductService} from '../../../../services/product.service';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {ImageService} from '../../../../services/image.service';
import {animate, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-product-list-admin',
  templateUrl: './product-list-admin.component.html',
  styleUrls: ['./product-list-admin.component.css'],
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
export class ProductListAdminComponent implements OnInit {

  productsState = true;
  selectedProduct: Product = null;
  products: Product[] = [];
  isLoadingProducts = false;
  previousPrice: number = 0;
  previousKeyword: string = '';
  previousPlatform: string = '';
  currentPrice: number = 0;
  currentPlatformName: string = '';
  currentSearchKeyword: string = '';
  error;
  pageNr: number = 1;
  pageSize: number= 10;
  totalElements: number= 0;

  constructor(private productService: ProductService,
              private route: ActivatedRoute,
              private router: Router,
              private imageService: ImageService) { }

  ngOnInit(): void {
    this.loadScript();
    this.listProducts();
  }

  listProducts(): void{
    console.log("Page nr: " + this.pageNr);
    this.route.queryParams.subscribe((params: Params) => {
      if (params.hasOwnProperty('name') && params['name'] != ''){
        this.currentSearchKeyword = params['name'];
        if (this.currentPlatformName !== ''){
          this.pageNr = 1;
          this.currentPlatformName = '';
          this.currentPrice = 0;
        }

        this.handleSearchProducts();
      }
      else{
        if (this.currentSearchKeyword !== ''){
          this.pageNr = 1;
          this.currentSearchKeyword = '';
        }
        if (params.hasOwnProperty('platform')){
          this.currentPlatformName = params['platform'];
        }
        if (this.previousPlatform !== this.currentPlatformName ){
          this.pageNr = 1;
        }
        this.previousPlatform = this.currentPlatformName;
        if (params.hasOwnProperty('price')){
          this.currentPrice = params['price'];
        }
        this.previousPrice = this.currentPrice;
        this.handleListProducts();
      }
    });
  }

  onHandleError():void{
    this.error = null;
  }

  loadScript(): void{
    let body = document.body as HTMLDivElement;
    let script = document.createElement('script');
    script.innerHTML = '';
    script.src = '../../assets/js/filter-desktop.js';
    script.async = true;
    script.defer = true;
    body.appendChild(script);
  }

  handleListProducts(): void{
    this.isLoadingProducts = true;
    this.productService.getProductListPaginate(
      this.pageNr,
      this.pageSize,
      this.currentPlatformName,
      this.currentPrice,
      this.productsState).subscribe(
      this.processResult(),
      this.processError()
    );
  }

  processResult(){
    return data => {
      this.isLoadingProducts = false;
      this.products = data.objectsList;
      this.pageNr = data.pageNumber;
      this.pageSize = data.pageSize;
      this.totalElements = data.totalElements;
      console.log(data);
    };
  }

  processError(){
    return error1 => {
      this.pageNr = 1;
      this.isLoadingProducts = false;
      this.error = error1.error.message;
      this.products = [];
    };
  }


  handleSearchProducts(): void{
    this.isLoadingProducts = true;
    if (this.previousKeyword !== this.currentSearchKeyword){
      this.pageNr = 1;
    }
    this.previousKeyword = this.currentSearchKeyword;
    this.productService.searchProductsPaginate(this.pageNr, this.pageSize, this.currentSearchKeyword).subscribe(
      this.processResult(),
      this.processError()
    );
  }

  onClearFilters(): void{
    this.previousPrice = 0;
    this.currentPrice = 0;
    this.previousKeyword = '';
    this.previousPlatform = '';
    this.currentSearchKeyword = '';
    this.currentPlatformName = '';
    this.pageNr = 1;
    this.router.navigate(['admin/products'], { queryParams: { platform: 'all', price: '0'}});
    this.listProducts();
  }

  updatePageSize(size: number): void{
    this.pageSize = size;
    this.pageNr = 1;
    this.listProducts();
  }

  handleProductImage(imagePath: string): string{
    return this.productService.handleProductImage(imagePath);
  }

  checkIfProductIsSelected(id: number): boolean{
    if (this.selectedProduct == null){
      return false;
    }
    else{
      return this.selectedProduct.id === id;
    }
  }

  handleProductSelect(product: Product): void{
    if ((this.selectedProduct != null) && (product.id === this.selectedProduct.id)){
      this.selectedProduct = null;
    }
    else{
      this.selectedProduct = product;
    }
  }

  handleEditNavigation(id: number): void{
    this.previousPrice = 0;
    this.currentPrice = 0;
    this.previousKeyword = '';
    this.previousPlatform = '';
    this.currentSearchKeyword = '';
    this.currentPlatformName = '';
    this.pageNr = 1;
    this.selectedProduct = null;
    const navigateUrl = `/admin/products/edit/${id}`;
    this.router.navigate([navigateUrl]);
  }

  handleProductsState(state: boolean){
    this.productsState = state;
    this.listProducts();
  }

  handleProductDeactivate(id: number): void{
    this.productService.deactivateProduct(id).subscribe(
      data => {
        this.selectedProduct = null;
        this.listProducts();
      },
      error => {
        this.selectedProduct = null;
        this.listProducts();
        console.log(error);
        this.error = error.error.message;
      }
    );
  }

  handleProductView(product: Product): void{
    this.previousPrice = 0;
    this.currentPrice = 0;
    this.previousKeyword = '';
    this.previousPlatform = '';
    this.currentSearchKeyword = '';
    this.currentPlatformName = '';
    this.pageNr = 1;
    this.selectedProduct = null;
    const navigateUrl = `/products/${product.id}`;
    this.router.navigate([navigateUrl]);
  }
}
