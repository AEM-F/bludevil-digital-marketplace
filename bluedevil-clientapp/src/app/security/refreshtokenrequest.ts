export class RefreshTokenRequest{
  public constructor(private refreshToken: string) {
  }

  get getRefreshToken() {
    return this.refreshToken;
  }

  set setRefreshToken(value) {
    this.refreshToken = value;
  }
}
