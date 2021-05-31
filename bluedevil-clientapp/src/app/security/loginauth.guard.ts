import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {AuthenticationService} from '../services/authentication.service';
import {Observable} from 'rxjs';

@Injectable({ providedIn: 'root'})
export class LoginAuthGuard implements CanActivate {
  constructor(private router: Router, private authenticationService: AuthenticationService) {
  }

  canActivate(route: ActivatedRouteSnapshot,
              state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const userJwt = this.authenticationService.getUserValue;
    if (userJwt == null){
      // user is logged in
      return true;
    }else{
      // console.log(userJwt);
      this.router.navigate(['/']);
      return false;
    }
  }
}
