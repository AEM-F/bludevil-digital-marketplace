import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ProductPlatform} from "../common/productplatform";
import {PaginationResponse} from "../common/paginationresponse";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class ProductPlatformService {

  private baseUrl= 'http://localhost:8080/api/productPlatforms?retrieval=all';

  constructor(private http: HttpClient) { }

  getPlatformList():Observable<ProductPlatform[]>{
    return this.http.get<ProductPlatform[]>(this.baseUrl).pipe(
      map(response => {
        return response;
      })
    );
  }
}
