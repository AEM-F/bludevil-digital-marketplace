import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = environment.apiBaseUrl + '/api/users';

  constructor(private http: HttpClient) { }

  public getUserById(id: number): Observable<any>{
    const endpoint = `${this.baseUrl}/${id}`;
    return this.http.get<any>(endpoint);
  }

  public countAllUsers(): Observable<any>{
    const endpoint = `${this.baseUrl}/statistics/countAll`;
    return this.http.get(endpoint);
  }

  public getUserRoleRatioData(): Observable<any>{
    const endpoint = `${this.baseUrl}/statistics/userRoleRatio`;
    return this.http.get(endpoint);
  }

  public getDailyRegisteredUsers(): Observable<any>{
    const endpoint = `${this.baseUrl}/statistics/dailyRegistered`;
    return this.http.get(endpoint);
  }

}
