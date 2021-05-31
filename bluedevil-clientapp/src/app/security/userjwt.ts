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

  setToken(value: string): void {
    this.token = value;
  }

  get getType(): string {
    return this.type;
  }

  setType(value: string): void {
    this.type = value;
  }

  get getRefreshToken(): string {
    return this.refreshToken;
  }

  setRefreshToken(value: string): void {
    this.refreshToken = value;
  }

  get getId(): number {
    return this.id;
  }

  setId(value: number): void {
    this.id = value;
  }

  get getRefreshTokenExp(): Date {
    return this.refreshTokenExp;
  }

  setRefreshTokenExp(value: Date): void {
    this.refreshTokenExp = value;
  }
}
