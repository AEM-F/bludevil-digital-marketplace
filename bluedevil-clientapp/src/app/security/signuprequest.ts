export class SignupRequest{
  public constructor(private email: string, private password: string) {
  }

  get getEmail(): string {
    return this.email;
  }

  set setEmail(email: string){
    this.email = email;
  }

  get getPassword(): string {
    return this.password;
  }

  set setPassword(password: string){
    this.password = password;
  }
}
