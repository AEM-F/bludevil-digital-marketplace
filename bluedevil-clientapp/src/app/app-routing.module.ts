import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {ProductListComponent} from './components/product-list/product-list.component';
import {PageNotFoundComponent} from './components/page-not-found/page-not-found.component';
import {ProductDetailsComponent} from './components/product-details/product-details.component';
import {ProductCreationComponent} from './components/admin/products/product-creation/product-creation.component';
import {ProductEditComponent} from './components/admin/products/product-edit/product-edit.component';
import {ProductManageComponent} from './components/admin/products/product-manage/product-manage.component';
import {LoginComponent} from './components/login/login.component';
import {AdminAuthGuard} from './security/adminauth.guard';
import {LoginAuthGuard} from './security/loginauth.guard';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent, canActivate: [LoginAuthGuard]},
  {path: 'products/:id', component: ProductDetailsComponent},
  {path: 'products', component: ProductListComponent},
  {path: 'admin/products/create', component: ProductCreationComponent, canActivate: [AdminAuthGuard]},
  {path: 'admin/products/edit/:id', component: ProductEditComponent, canActivate: [AdminAuthGuard]},
  {path: 'admin/products', component: ProductManageComponent, canActivate: [AdminAuthGuard]},
  {path: 'not-found', component: PageNotFoundComponent},
  {path: '**', redirectTo: '/not-found'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
