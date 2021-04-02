import { Component, OnInit } from '@angular/core';
import {ProductPlatformService} from "../../services/product-platform.service";
import {ProductPlatform} from "../../common/productplatform";
import {ActivatedRoute, Params, Router} from '@angular/router';

@Component({
  selector: 'app-product-platform-menu',
  templateUrl: './product-platform-menu.component.html',
  styleUrls: ['./product-platform-menu.component.css']
})
export class ProductPlatformMenuComponent implements OnInit {

  isLoadingPlatforms= false;
  platforms: ProductPlatform[];
  currentPlatform: string;
  error;

  constructor(private platformService: ProductPlatformService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe((params: Params) => {
      if (params.hasOwnProperty('platform')){
        this.currentPlatform = params['platform'];
      }
    });
    this.listPlatforms();
  }

  listPlatforms():void{
    this.isLoadingPlatforms= true;
    this.platformService.getPlatformList().subscribe(
      data => {
        this.isLoadingPlatforms= false;
        this.platforms = data;
      },
      error1 => {
        this.isLoadingPlatforms= false;
        this.error= error1.error.message;
      }
    );
  }

  onHandleError():void{
    this.error= null;
  }

  onPlatformSubmit(platform: string): void{
    const searchParam = this.route.snapshot.queryParams['name'];
    if (searchParam != null){
      this.router.navigate([], {relativeTo: this.route , queryParams: { platform: platform}});
    }
    else{
      this.router.navigate([], {relativeTo: this.route ,queryParams: {platform: platform}, queryParamsHandling: 'merge'});
    }
  }

  checkIfPlatformIsActive(platform: string){
    if (platform !== this.currentPlatform){
      return false;
    }
    else{
      return true;
    }
  }

}
