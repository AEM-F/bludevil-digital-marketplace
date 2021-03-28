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

  private baseUrl = environment.apiBaseUrl+'/api/products';

  constructor(private http: HttpClient) { }

  getProductList(platform:string): Observable<Product[]>{
    let searchUrl = `${this.baseUrl}`;
    if ( platform != null && platform !== ''){
      searchUrl += `/platform/${platform}`;
    }

    return this.http.get<PaginationResponse<Product>>(searchUrl).pipe(
      map(response => {
        return response.objectsList;
      })
    );
  }

  searchProducts(keyword: string): Observable<Product[]> {
    let searchUrl = `${this.baseUrl}/search/${keyword}`;

    return this.http.get<PaginationResponse<Product>>(searchUrl).pipe(
      map(response => {
        return response.objectsList;
      })
    );
  }

  getProductById(id: number): Observable<Product>{
    let searchUrl = `${this.baseUrl}/${id}`;
    return this.http.get<Product>(searchUrl);
  }
}


