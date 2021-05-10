import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserJwt} from './userjwt';
import {environment} from '../../environments/environment';
import {AuthenticationService} from '../services/authentication.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor{
  // initialise the authenticationService
  constructor(private authenticationService: AuthenticationService) {
  }
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add the auth header with the jwt if the user is logged in and the request is to the bludevil api
    const user = this.authenticationService.getUserValue;
    const isLoggedIn = user && user.getToken;
    const isApiBluDevil = req.url.startsWith(environment.apiBaseUrl);
    if (isLoggedIn && isApiBluDevil){
      req = req.clone({
        setHeaders: {
          Authorization: `${user.getType} ${user.getToken}`
        }
      });
    }
    return next.handle(req);
  }
}
