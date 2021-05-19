import {Component, Inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {DOCUMENT} from '@angular/common';
import {animate, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-search-product-admin',
  templateUrl: './search-product-admin.component.html',
  styleUrls: ['./search-product-admin.component.css'],
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
export class SearchProductAdminComponent implements OnInit {

  selectedOption = 'products';

  constructor(private router: Router, private route: ActivatedRoute, @Inject(DOCUMENT) private document: Document) { }

  ngOnInit(): void {
  }

  onChangeSearchOption(option: string): void{
    const searchOption = option.toLowerCase();
    if (searchOption === 'products'){
      this.selectedOption = searchOption;
    }
    else if (searchOption === 'platforms'){
      this.selectedOption = searchOption;
    }
    else if (searchOption === 'genres'){
      this.selectedOption = searchOption;
    }
  }

  onSearchProduct(value: string): void{
    if (this.selectedOption === 'products'){
      this.router.navigate([], {relativeTo: this.route, queryParams: {name: `${value}`}, queryParamsHandling: 'merge'});
      const productSection = this.document.getElementById('products-section');
      productSection.scrollIntoView();
    }
    else if (this.selectedOption === 'platforms'){
      this.router.navigate([], {relativeTo: this.route, queryParams: {platformName: `${value}`}, queryParamsHandling: 'merge'});
      const platformsSection = this.document.getElementById('platforms-section');
      platformsSection.scrollIntoView();
    }
    else if (this.selectedOption === 'genres'){
      this.router.navigate([], {relativeTo: this.route, queryParams: {genreName: `${value}`}, queryParamsHandling: 'merge'});
      const genreSection = this.document.getElementById('genres-section');
      genreSection.scrollIntoView();
    }
  }

}
