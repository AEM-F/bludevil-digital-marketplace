import {AuthenticationService} from '../services/authentication.service';
import {UserJwt} from './userjwt';

export function appIntializer(authenticationService: AuthenticationService){
  const userJwt: UserJwt = authenticationService.getUserValue;
  if (userJwt){
    authenticationService.refreshToken();
  }
  // return () => new Promise(resolve => {
  //   // attempt to refresh the token on start-up
  //   authenticationService.refreshToken().subscribe().add(resolve);
  // });
}
