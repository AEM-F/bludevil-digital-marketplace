import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, timer} from 'rxjs';
import {UserJwt} from '../security/userjwt';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';
import {LoginRequest} from '../security/loginrequest';
import {debounce, map} from 'rxjs/operators';
import {RefreshTokenRequest} from '../security/refreshtokenrequest';
import {UserDetailsResponse} from '../common/userdetailsresponse';
import {TokenLocalStorageService} from './token-local-storage.service';
import {SignupRequest} from '../security/signuprequest';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private baseUrl = environment.apiBaseUrl + '/api/auth';
  private userSubject: BehaviorSubject<UserJwt>;
  public user: Observable<UserJwt>;
  private refreshTokenTimeout;

  constructor(private router: Router, private http: HttpClient, private tokenStorage: TokenLocalStorageService) {
    this.userSubject = new BehaviorSubject<UserJwt>(null);
    this.user = this.userSubject.asObservable();
    if (this.tokenStorage.getUser() != null){
      this.userSubject.next(this.tokenStorage.getUser());
    }
  }

  public get getUserValue(): UserJwt{
    return this.userSubject.value;
  }

  public get getUserRoles(): string[] | null{
    if (this.getUserValue != null){
      const  jwtToken = JSON.parse(atob(this.getUserValue.getToken.split('.')[1]));
      return jwtToken.scopes;
    }
    else{
      return null;
    }
  }

  login(email: string, password: string): Observable<UserJwt>{
    const endpoint = `${this.baseUrl}/signin`;
    const requestObj: LoginRequest = new LoginRequest(email, password);
    return this.http.post<any>(endpoint, requestObj, { withCredentials: true}).pipe(map(user => {
      const userJwt: UserJwt = new UserJwt(user.token, user.type, user.refreshToken, user.id, new Date(user.refreshTokenExp + "Z"));
      this.tokenStorage.saveUser(userJwt);
      // console.log(userJwt);
      this.userSubject.next(userJwt);
      this.startRefreshTokenTimer();
      return userJwt;
    }));
  }

  signUp(email: string, password: string): Observable<any>{
    const endpoint = `${this.baseUrl}/signup`;
    const requestObj: SignupRequest = new SignupRequest(email, password);
    return this.http.post<any>(endpoint, requestObj, { withCredentials: true});
  }

  logout(){
    this.stopRefreshTokenTimer();
    this.tokenStorage.clearUser();
    this.userSubject.next(null);
    this.router.navigate(['/login']);
  }

  refreshToken(){
    const endpoint = `${this.baseUrl}/refreshToken`;
    const refreshTokenRequest: RefreshTokenRequest = new RefreshTokenRequest(this.getUserValue.getRefreshToken);
    return this.http.post<any>(endpoint,
      refreshTokenRequest, {withCredentials: true}).pipe(map((response) => {
        const userJwt = new UserJwt(response.token,
          this.getUserValue.getType,
          response.refreshToken,
          this.getUserValue.getId,
          new Date(response.refreshTokenExp + "Z"));
        this.tokenStorage.saveUser(userJwt);
        this.userSubject.next(userJwt);
        this.startRefreshTokenTimer();
        return userJwt;
    }));
  }

  getUserInfo(): Observable<UserDetailsResponse>{
    const endpoint = `${this.baseUrl}/userInfo/${this.getUserValue.getRefreshToken}`;
    return this.http.get<any>(endpoint).pipe(map((response) => {
      return new UserDetailsResponse(response.firstName, response.lastName, response.email, response.roles, response.imagePath);
    }));
  }

  private startRefreshTokenTimer(){
    // parsing json object from base64 encoded jwt token
    const  jwtToken = JSON.parse(atob(this.getUserValue.getToken.split('.')[1]));
    // console.log(jwtToken);
    // setting a timeout of 1 min before the token expires
    const expires = new Date(jwtToken.exp * 1000);
    const timeout = expires.getTime() - Date.now() - (60 * 1000);
    this.refreshTokenTimeout = setTimeout(() => this.refreshToken().subscribe(), timeout);
  }

  private stopRefreshTokenTimer(){
    clearTimeout(this.refreshTokenTimeout);
  }

  public checkRefreshToken(): boolean{
    const user = this.getUserValue;
    if (user !== null){
      console.log(user);
      const tokenExpDate: Date = user.getRefreshTokenExp;
      const nowDate: Date = new Date(Date.now() - (60 * 1000));
      if (tokenExpDate > this.convertLocalDateToUTCIgnoringTimezone(nowDate)){
        return false;
      }
      else{
        return true;
      }
    }else{
      return false;
    }
  }

  private convertLocalDateToUTCIgnoringTimezone(date: Date): Date{
    const timestamp = Date.UTC(
      date.getFullYear(),
      date.getMonth(),
      date.getDate(),
      date.getHours(),
      date.getMinutes(),
      date.getSeconds(),
      date.getMilliseconds(),
    );

    return new Date(timestamp);
  }

  private convertUTCToLocalDateIgnoringTimezone(utcDate: Date): Date{
    return new Date(
      utcDate.getUTCFullYear(),
      utcDate.getUTCMonth(),
      utcDate.getUTCDate(),
      utcDate.getUTCHours(),
      utcDate.getUTCMinutes(),
      utcDate.getUTCSeconds(),
      utcDate.getUTCMilliseconds(),
    );
  }
}
