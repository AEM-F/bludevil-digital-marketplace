export class LoginRequest{
  public constructor(private email: string, private password: string) {
  }

  get getEmail(): string {
    return this.email;
  }

  set setEmail(value: string) {
    this.email = value;
  }

  get getPassword(): string {
    return this.password;
  }

  set setPassword(value: string) {
    this.password = value;
  }
}
