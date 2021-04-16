import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../services/product.service";
import {Product} from "../../common/product";
import {ProductPlatformService} from "../../services/product-platform.service";
import {ProductPlatform} from "../../common/productplatform";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {ImageService} from '../../services/image.service';
import {environment} from '../../../environments/environment';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

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
    this.productService.getProductListPaginate(this.pageNr, this.pageSize , this.currentPlatformName, this.currentPrice, true).subscribe(
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
    this.router.navigate(['/products'], { queryParams: { platform: 'all', price: '0'}});
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
}
