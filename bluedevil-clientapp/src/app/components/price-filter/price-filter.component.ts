import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ProductValidators} from '../validators/product.validators';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-price-filter',
  templateUrl: './price-filter.component.html',
  styleUrls: ['./price-filter.component.css']
})
export class PriceFilterComponent implements OnInit {
  priceGroup = new FormGroup({
    price: new FormControl('', [
      Validators.pattern(ProductValidators.positiveNumberPattern),
      ProductValidators.cannotContainSpace
    ])
  });

  get price(){
    return this.priceGroup.get('price');
  }

  constructor(private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
  }

  onPriceSubmit() {
    if (!this.price.errors){
      const searchParam = this.route.snapshot.queryParams['name'];
      if (this.price.value === ''){
        this.priceGroup.patchValue({price: '0'});
      }
      console.log(searchParam);
      if (searchParam != null){
        this.router.navigate([], {relativeTo: this.route ,queryParams: {price: this.price.value}});
      }
      else{
        this.router.navigate([], {relativeTo: this.route ,queryParams: {price: this.price.value}, queryParamsHandling: 'merge'});
      }
    }
  }
}
