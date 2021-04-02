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

  constructor(private http: HttpClient) { }

  getProductListPaginate(page: number, size: number, platform: string, price: number): Observable<PaginationResponse<Product>>{
    let searchUrl = `${this.baseUrl}`;
    if ( platform != null && platform !== '' && platform !== 'all'){
      searchUrl += `/platform/${platform}`;
    }
    searchUrl += `?page=${page}&size=${size}`;
    if ( price != null && price > 0){
      searchUrl += `&price=${price}`;
    }
    return this.http.get<PaginationResponse<Product>>(searchUrl).pipe(
      map(response => {
        return response;
      })
    );
  }

  searchProductsPaginate(page: number, size: number, keyword: string): Observable<PaginationResponse<Product>>{
    let searchUrl = `${this.baseUrl}/search/${keyword}`;
    searchUrl += `?page=${page}&size=${size}`;
    return this.http.get<PaginationResponse<Product>>(searchUrl).pipe(
      map(response => {
        return response;
      })
    );
  }

  getProductById(id: number): Observable<Product>{
    let searchUrl = `${this.baseUrl}/${id}`;
    return this.http.get<Product>(searchUrl);
  }
}


