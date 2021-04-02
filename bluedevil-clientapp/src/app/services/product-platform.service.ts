import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ProductPlatform} from "../common/productplatform";
import {PaginationResponse} from "../common/paginationresponse";
import {map} from "rxjs/operators";
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductPlatformService {

  private baseUrl = environment.apiBaseUrl + '/api/productPlatforms';

  constructor(private http: HttpClient) { }

  getPlatformList(): Observable<ProductPlatform[]>{
    const searchUrl = `${this.baseUrl}?retrieval=all`;
    return this.http.get<ProductPlatform[]>(searchUrl).pipe(
      map(response => {
        return response;
      })
    );
  }
}
