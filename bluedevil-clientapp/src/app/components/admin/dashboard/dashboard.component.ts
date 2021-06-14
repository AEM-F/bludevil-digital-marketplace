import {Component, OnDestroy, OnInit} from '@angular/core';
import {animate, style, transition, trigger} from '@angular/animations';
import {ProductService} from '../../../services/product.service';
import {UserService} from '../../../services/user.service';
import {WebViewsService} from '../../../services/web-views.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
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
export class DashboardComponent implements OnInit, OnDestroy {

  userRatioData = [
    {name: 'Admins', value: 2},
    {name: 'Members', value: 5}
  ];

  // productPlatformData = [
  //   {name: 'Steam', value : 1},
  //   {name: 'Battle.net', value : 1},
  //   {name: 'Origin', value : 1},
  //   {name: 'Epic', value : 1},
  //   {name: 'Gog', value : 1}
  // ];
  // productVideoGameData = [
  //   {name: 'action', value : 1},
  //   {name: 'sandbox', value: 2},
  //   {name: 'rpg', value: 1},
  //   {name: 'open-world', value: 0}
  // ];
  productPlatformData = [];
  productVideoGameData = [];

  userRatioDataColors = [
    {name: 'Admins', value: '#043dd6'},
    {name: 'Members', value: '#cca716'}
  ];

  totalProducts = 0;
  totalUsers = 0;
  totalViews = 0;
  dailyRegisteredUsers = 0;
  dailyViews = 0;
  private timer;
  constructor(private productService: ProductService, private userService: UserService, private viewsService: WebViewsService) { }

  ngOnInit(): void {
    this.getAllStatistics();
    this.startTimer();
  }

  startTimer(){
    this.timer = setInterval(() => {
      this.getAllStatistics();
    }, 30000);
  }

  stopTimer(){
    clearInterval(this.timer);
  }

  getAllStatistics(){
    this.countAllProducts();
    this.countAllUsers();
    this.countTotalViews();
    this.getDailyRegisteredUsers();
    this.countDailyViews();
    this.getUserRoleRatioData();
    this.getAllProductsByPlatformsData();
    this.getAllProductsByGenresData();
  }

  countAllProducts(){
    this.productService.countAllProducts().subscribe(data => {
      this.totalProducts = data[0].value;
    }, error => {
      const errorMsg = error.error.message || error.message;
      alert(errorMsg);
      this.totalProducts = 0;
    });
  }

  getAllProductsByPlatformsData(){
    this.productService.countAllProductsByPlatform().subscribe(data => {
      this.productPlatformData = data;
    }, error => {
      const errorMsg = error.error.message || error.message;
      alert(errorMsg);
      this.productPlatformData = [];
    });
  }

  getAllProductsByGenresData(){
    this.productService.countAllProductsByGenre().subscribe(data => {
      this.productVideoGameData = data;
    }, error => {
      const errorMsg = error.error.message || error.message;
      alert(errorMsg);
      this.productVideoGameData = [];
    });
  }

  getUserRoleRatioData(){
    this.userService.getUserRoleRatioData().subscribe(data => {
      this.userRatioData = data;
    }, error => {
      const errorMsg = error.error.message || error.message;
      alert(errorMsg);
      this.userRatioData = [];
    });
  }

  countAllUsers(){
    this.userService.countAllUsers().subscribe(data => {
      this.totalUsers = data.value;
    }, error => {
      const errorMsg = error.error.message || error.message;
      alert(errorMsg);
      this.totalUsers = 0;
    });
  }

  getDailyRegisteredUsers(){
    this.userService.getDailyRegisteredUsers().subscribe(data => {
      this.dailyRegisteredUsers = data.value;
    }, error => {
      const errorMsg = error.error.message || error.message;
      alert(errorMsg);
      this.dailyRegisteredUsers = 0;
    });
  }

  countTotalViews(){
    this.viewsService.getAllUserViews().subscribe(data => {
      this.totalViews = data.value;
    }, error => {
      const errorMsg = error.error.message || error.message;
      alert(errorMsg);
      this.totalViews = 0;
    });
  }

  countDailyViews(){
    this.viewsService.getAllUserViews().subscribe(data => {
      this.dailyViews = data.value;
    }, error => {
      const errorMsg = error.error.message || error.message;
      alert(errorMsg);
      this.dailyViews = 0;
    });
  }

  ngOnDestroy(): void {
    this.stopTimer();
  }

}
