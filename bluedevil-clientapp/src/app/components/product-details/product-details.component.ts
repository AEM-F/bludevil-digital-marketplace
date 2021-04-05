import { Component, OnInit } from '@angular/core';
import {Product} from '../../common/product';
import {ActivatedRoute, Router} from '@angular/router';
import {ProductService} from '../../services/product.service';
import {SoftwareProduct} from '../../common/softwareproduct';
import {Videogame} from '../../common/videogame';
import {Genre} from '../../common/genre';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {

  product: Product = null;
  isLoadingProduct = false;
  error;

  constructor(private productService: ProductService, private router:Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(() => {
      this.handleProductDetails();
    });
  }

  private handleProductDetails(): void {
    this.isLoadingProduct = true;
    const productId: number = +this.route.snapshot.paramMap.get('id');
    this.productService.getProductById(productId).subscribe(
      data => {
        this.isLoadingProduct = false;
        this.product = data;
        console.log(data);
      },
      error1 => {
        this.isLoadingProduct = false;
        this.error = error1.error.message;
        this.product = null;
      }
    );
  }

  get productAsVideoGame(): Videogame{
   return (this.product as Videogame);
  }

  onHandleError(): void{
    this.error = null;
    this.router.navigate(['/products']);
  }

  handleProductImage(imagePath: string): string{
    return this.productService.handleProductImage(imagePath);
  }
}
