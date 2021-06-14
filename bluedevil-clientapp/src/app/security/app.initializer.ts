import {AuthenticationService} from '../services/authentication.service';
import {UserJwt} from './userjwt';
import {TokenLocalStorageService} from '../services/token-local-storage.service';
import {ConnectableObservable} from 'rxjs';
import {publish} from 'rxjs/operators';
import {WebViewsService} from '../services/web-views.service';

export function appIntializer(authenticationService: AuthenticationService,
                              tokenStorage: TokenLocalStorageService,
                              webViewsService: WebViewsService){
  return () => {
    if (webViewsService.checkIfUserVisited() === false){
      webViewsService.addView().subscribe();
    }
    const userJwt: UserJwt = authenticationService.getUserValue;
    if (userJwt){
      const isValidRefreshToken: boolean = authenticationService.checkRefreshToken();
      console.log(isValidRefreshToken);
      if (isValidRefreshToken === true){
        const myConnectableObservable: ConnectableObservable<any> = authenticationService.refreshToken().pipe(
          publish()) as ConnectableObservable<any>;
        myConnectableObservable.connect();
      }
      else{
        this.logout();
      }
    }
  };

}
