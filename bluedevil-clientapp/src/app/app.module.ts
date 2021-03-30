import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProductListComponent } from './components/product-list/product-list.component';
import { HomeComponent } from './components/home/home.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import {HttpClientModule} from "@angular/common/http";
import {ProductService} from "./services/product.service";
import { FirstToUpperPipe } from './common/pipes/first-to-upper.pipe';
import {ProductPlatformService} from "./services/product-platform.service";
import { ProductPlatformMenuComponent } from './components/product-platform-menu/product-platform-menu.component';
import { ScrollToTopComponent } from './components/scroll-to-top/scroll-to-top.component';
import { SearchProductComponent } from './components/search-product/search-product.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {NgxPaginationModule} from 'ngx-pagination';

@NgModule({
  declarations: [
    AppComponent,
    ProductListComponent,
    HomeComponent,
    PageNotFoundComponent,
    FirstToUpperPipe,
    ProductPlatformMenuComponent,
    ScrollToTopComponent,
    SearchProductComponent,
    ProductDetailsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgbModule
  ],
  providers: [
    ProductService,
    ProductPlatformService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
