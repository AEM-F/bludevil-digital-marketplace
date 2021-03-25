import { Component, OnInit } from '@angular/core';
import {ProductPlatformService} from "../../services/product-platform.service";
import {ProductPlatform} from "../../common/productplatform";

@Component({
  selector: 'app-product-platform-menu',
  templateUrl: './product-platform-menu.component.html',
  styleUrls: ['./product-platform-menu.component.css']
})
export class ProductPlatformMenuComponent implements OnInit {

  isLoadingPlatforms= false;
  platforms: ProductPlatform[];
  error;

  constructor(private platformService: ProductPlatformService) { }

  ngOnInit(): void {
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

}
