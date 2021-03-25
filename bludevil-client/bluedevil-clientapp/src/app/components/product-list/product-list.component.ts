import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../services/product.service";
import {Product} from "../../common/product";
import {ProductPlatformService} from "../../services/product-platform.service";
import {ProductPlatform} from "../../common/productplatform";
import {ActivatedRoute, Params, Router} from "@angular/router";

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

  products: Product[];
  isLoadingProducts= false;
  currentPlatformName: string='';
  currentSearchKeyword: string= '';
  searchMode: boolean;
  error;
  constructor(private productService: ProductService,private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
    this.loadScript();
    this.listProducts();
  }


  listProducts():void{
    this.route.queryParams.subscribe((params:Params)=>{
      if(params.hasOwnProperty('name') && params['name'] != ''){
        this.currentSearchKeyword = params['name'];
        this.currentPlatformName= '';
        this.handleSearchProducts();
      }
      else if(params.hasOwnProperty('platform')){
        this.currentPlatformName= params['platform'];
        this.currentSearchKeyword= '';
        this.handleListProducts();
      }
      else{
        this.currentSearchKeyword= '';
        this.currentPlatformName='';
        this.handleListProducts();
      }

    });
  }

  onHandleError():void{
    this.error= null;
  }

  loadScript():void{
    let body = <HTMLDivElement> document.body;
    let script = document.createElement('script');
    script.innerHTML = '';
    script.src = '../../assets/js/filter-desktop.js';
    script.async = true;
    script.defer = true;
    body.appendChild(script);
  }

  handleListProducts():void{
    this.isLoadingProducts= true;
    this.productService.getProductList(this.currentPlatformName).subscribe(
      data => {
        this.isLoadingProducts= false;
        this.products = data;
      },
      error1 => {
        this.isLoadingProducts= false;
        this.error= error1.error.message;
        this.products=[];
      }
    );
  }

  handleSearchProducts():void{
    this.isLoadingProducts = true;
    this.productService.searchProducts(this.currentSearchKeyword).subscribe(
      data => {
        this.isLoadingProducts= false;
        this.products = data;
      },
      error1 => {
        this.isLoadingProducts= false;
        this.error= error1.error.message;
        this.products=[];
      }
    );
  }

  onClearFilters(){
    this.currentSearchKeyword='';
    this.currentPlatformName='';
    this.router.navigate(['/products']);
  }

}
