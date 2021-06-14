import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {CookieService} from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class WebViewsService {

  private VIEW_KEY = 'has-visited';
  private baseUrl = environment.apiBaseUrl + '/api/views';
  constructor(private http: HttpClient, private cookieService: CookieService) { }

  public getAllUserViews(): Observable<any>{
    const endpoint = `${this.baseUrl}/countAll`;
    return this.http.get(endpoint);
  }

  public getDailyViews(): Observable<any>{
    const endpoint = `${this.baseUrl}/countAllDaily`;
    return this.http.get(endpoint);
  }

  public addView(): Observable<any>{
    if (this.cookieService.check(this.VIEW_KEY) === false){
      this.cookieService.set(this.VIEW_KEY, 'true');
    }
    const endpoint = `${this.baseUrl}`;
    return this.http.post(endpoint, null);
  }

  public checkIfUserVisited(): boolean{
    return this.cookieService.check(this.VIEW_KEY);
  }
}
