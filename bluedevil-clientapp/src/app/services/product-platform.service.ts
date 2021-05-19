import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ProductPlatform} from '../common/productplatform';
import {map} from 'rxjs/operators';
import {environment} from '../../environments/environment';
import {PaginationResponse} from '../common/paginationresponse';

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

  getPlatformListPaginate(page: number, size: number): Observable<PaginationResponse<ProductPlatform>>{
    const endpoint = `${this.baseUrl}?page=${page}&size=${size}&retrieval=page`;
    return this.http.get<PaginationResponse<ProductPlatform>>(endpoint);
  }

  createPlatform(platform: ProductPlatform): Observable<any>{
    return this.http.post(this.baseUrl, platform);
  }

  updatePlatform(platform: ProductPlatform, platformId: number): Observable<any>{
    const endpoint = `${this.baseUrl}/${platformId}`;
    return this.http.put(endpoint, platform);
  }

  checkNameValidity(platformName: string): Observable<any>{
    platformName = platformName.toLowerCase();
    const endpoint = `${this.baseUrl}/checkNameValidity/${platformName}`;
    return this.http.get(endpoint);
  }

  searchPlatformsPaginate(page: number, size: number, keyword: string): Observable<PaginationResponse<ProductPlatform>> {
    let searchUrl = `${this.baseUrl}/search/${keyword}`;
    searchUrl += `?page=${page}&size=${size}`;
    return this.http.get<PaginationResponse<ProductPlatform>>(searchUrl).pipe(
      map(response => {
        return response;
      })
    );
  }
}
