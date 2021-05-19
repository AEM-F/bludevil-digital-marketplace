import {Injectable} from '@angular/core';
import {IUserTokenStorage} from './IUserTokenStorage';
import {UserJwt} from '../security/userjwt';

@Injectable({
  providedIn: 'root'
})
export class TokenLocalStorageService implements IUserTokenStorage{

  private USER_KEY = 'auth-user';

  constructor() { }

  clearUser(): void {
    localStorage.removeItem(this.USER_KEY);
  }

  getUser(): UserJwt | null {
    let user = localStorage.getItem(this.USER_KEY);
    if (user !== null && user !== undefined && user !== 'undefined'){
      // console.log(user);
      const userObj = JSON.parse(user);
      return new UserJwt(userObj.token, userObj.type, userObj.refreshToken, userObj.id, userObj.refreshTokenExp);
    }
    else {
      return null;
    }
  }

  saveUser(user: UserJwt): void {
    localStorage.removeItem(this.USER_KEY);
    if (user){
      localStorage.setItem(this.USER_KEY, JSON.stringify(user));
    }
  }
}
