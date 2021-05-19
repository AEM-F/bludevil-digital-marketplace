import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthenticationService} from '../services/authentication.service';

@Injectable({ providedIn: 'root'})
export class AdminAuthGuard implements CanActivate {
  constructor(private router: Router, private authenticationService: AuthenticationService) {
  }

  canActivate(route: ActivatedRouteSnapshot,
              state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    // will get the user jwt from authenticationService
    const userJwt = this.authenticationService.getUserValue;
    if (userJwt != null){
      // user is logged in
      const index = this.authenticationService.getUserRoles.indexOf('ADM');
      if (index > -1){
        return true;
      }
      else {
        this.router.navigate(['/'], { queryParams: {returnUrl: state.url}});
        return false;
      }
    }else{
      // user is not logged in => redirect to login
      this.router.navigate(['/login'], { queryParams: {returnUrl: state.url}});
      return false;
    }
  }
}
