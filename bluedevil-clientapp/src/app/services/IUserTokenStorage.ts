import {UserJwt} from '../security/userjwt';

export interface IUserTokenStorage {
  clearUser(): void;
  saveUser(user: UserJwt): void;
  getUser(): UserJwt | null;
}
