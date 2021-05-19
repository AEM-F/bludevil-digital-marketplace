import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {AuthenticationService} from '../services/authentication.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor{
  // initialise the authenticationService
  constructor(private authenticationService: AuthenticationService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError(err => {
        if ([401, 403, 417].includes(err.status) && this.authenticationService.getUserValue){
          // auto logout if 401,403 or 417 response returned from api
          this.authenticationService.logout();
        }
        // const error = (err && err.error && err.error.message) || err.statusText;
        // console.error(err);
        return throwError(err);
      })
    );
  }
}
