export class UserJwt{
  public constructor(private token: string,
                     private type: string,
                     private refreshToken: string,
                     private id: number,
                     private refreshTokenExp: Date) {
  }

  get getToken(): string {
    return this.token;
  }

  set setToken(value: string) {
    this.token = value;
  }

  get getType(): string {
    return this.type;
  }

  set setType(value: string) {
    this.type = value;
  }

  get getRefreshToken(): string {
    return this.refreshToken;
  }

  set setRefreshToken(value: string) {
    this.refreshToken = value;
  }

  get getId(): number {
    return this.id;
  }

  set setId(value: number) {
    this.id = value;
  }

  get getRefreshTokenExp(): Date {
    return this.refreshTokenExp;
  }

  set setRefreshTokenExp(value: Date) {
    this.refreshTokenExp = value;
  }
}
