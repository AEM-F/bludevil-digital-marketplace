import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {Product} from "../common/product";
import {PaginationResponse} from "../common/paginationresponse";
import { map } from 'rxjs/operators';
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private baseUrl = environment.apiBaseUrl + '/api/products';

  constructor(private http: HttpClient) {
  }

  handleProductImage(imagePath: string): string {
    if (imagePath != null && imagePath.length > 0 && imagePath !== '') {
      return imagePath;
    } else {
      return environment.apiBaseUrl + '/api/images/getImage/picture-not-available.jpg';
    }
  }

  getProductListPaginate(page: number, size: number, platform: string, price: number, productsState: boolean): Observable<PaginationResponse<Product>> {
    let searchUrl = `${this.baseUrl}`;
    if (platform != null && platform !== '' && platform !== 'all') {
      searchUrl += `/platform/${platform}`;
    }
    searchUrl += `?page=${page}&size=${size}`;
    if (price != null && price > 0) {
      searchUrl += `&price=${price}`;
    }
    searchUrl += `&state=${productsState}`;
    return this.http.get<PaginationResponse<Product>>(searchUrl).pipe(
      map(response => {
        return response;
      })
    );
  }

  searchProductsPaginate(page: number, size: number, keyword: string): Observable<PaginationResponse<Product>> {
    let searchUrl = `${this.baseUrl}/search/${keyword}`;
    searchUrl += `?page=${page}&size=${size}`;
    return this.http.get<PaginationResponse<Product>>(searchUrl).pipe(
      map(response => {
        return response;
      })
    );
  }

  getProductById(id: number): Observable<Product> {
    let searchUrl = `${this.baseUrl}/${id}`;
    return this.http.get<Product>(searchUrl);
  }

  createProduct(product: Product): Observable<any> {
    return this.http.post(this.baseUrl, product);
  }

  updateProduct(product: Product, id: number): Observable<any> {
    const endpoint = `${this.baseUrl}/${id}`;
    return this.http.put(endpoint, product);
  }

  deactivateProduct(id: number): Observable<any>{
    const endpoint = `${this.baseUrl}/${id}`;
    return this.http.delete(endpoint);
  }
}
