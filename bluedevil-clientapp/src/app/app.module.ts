import {APP_INITIALIZER, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProductListComponent } from './components/product-list/product-list.component';
import { HomeComponent } from './components/home/home.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {ProductService} from './services/product.service';
import { FirstToUpperPipe } from './common/pipes/first-to-upper.pipe';
import {ProductPlatformService} from './services/product-platform.service';
import { ProductPlatformMenuComponent } from './components/product-platform-menu/product-platform-menu.component';
import { ScrollToTopComponent } from './components/scroll-to-top/scroll-to-top.component';
import { SearchProductComponent } from './components/search-product/search-product.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { PriceFilterComponent } from './components/price-filter/price-filter.component';
import {ReactiveFormsModule} from '@angular/forms';
import { ProductCreationComponent } from './components/admin/products/product-creation/product-creation.component';
import { ProductEditComponent } from './components/admin/products/product-edit/product-edit.component';
import { ProductManageComponent } from './components/admin/products/product-manage/product-manage.component';
import { ProductPlatformManageComponent } from './components/admin/products/product-platform-manage/product-platform-manage.component';

import { ProductListAdminComponent } from './components/admin/products/product-list-admin/product-list-admin.component';
import {GenreManageComponent} from './components/admin/products/genre-manage/genre-manage.component';
import {ImageService} from './services/image.service';
import {GenreService} from './services/genre.service';
import { SearchProductAdminComponent } from './components/admin/products/search-product-admin/search-product-admin.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { LoginComponent } from './components/login/login.component';
import {appIntializer} from './security/app.initializer';
import {AuthenticationService} from './services/authentication.service';
import {JwtInterceptor} from './security/jwt.interceptor';
import {ErrorInterceptor} from './security/error.interceptor';
import {TokenLocalStorageService} from './services/token-local-storage.service';
import { UserDetailsComponent } from './components/user-details/user-details.component';
import {AuthGuard} from './security/auth.guard';
import {AdminAuthGuard} from './security/adminauth.guard';
import {LoginAuthGuard} from './security/loginauth.guard';
import {ProfileImageComponent} from './components/profile-image/profile-image.component';
import { ShoppingCartComponent } from './components/shopping-cart/shopping-cart.component';
import {CartStatusComponent} from './components/cart-status/cart-status.component';


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
    ProductDetailsComponent,
    PriceFilterComponent,
    ProductCreationComponent,
    ProductEditComponent,
    ProductManageComponent,
    ProductPlatformManageComponent,
    GenreManageComponent,
    ProductListAdminComponent,
    SearchProductAdminComponent,
    LoginComponent,
    UserDetailsComponent,
    ProfileImageComponent,
    ShoppingCartComponent,
    CartStatusComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        NgbModule,
        ReactiveFormsModule,
        BrowserAnimationsModule
    ],
  providers: [
    AuthenticationService,
    ProductService,
    ProductPlatformService,
    ImageService,
    GenreService,
    TokenLocalStorageService,
    AuthGuard,
    AdminAuthGuard,
    LoginAuthGuard,
    { provide: APP_INITIALIZER, useFactory: appIntializer, multi: true, deps: [AuthenticationService, TokenLocalStorageService] },
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
