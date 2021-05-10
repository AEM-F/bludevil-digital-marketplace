import { Component, OnInit } from '@angular/core';
import {Product} from '../../common/product';
import {ActivatedRoute, Router} from '@angular/router';
import {ProductService} from '../../services/product.service';
import {Videogame} from '../../common/videogame';
import {animate, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css'],
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
export class ProductDetailsComponent implements OnInit {

  product: Product = null;
  isLoadingProduct = false;
  error;

  constructor(private productService: ProductService, private router: Router, private route: ActivatedRoute) { }

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
