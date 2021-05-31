export class UserDetailsRequest{
  public constructor(public refreshToken: string,
                     public firstName: string,
                     public lastName: string,
                     public email: string) {
  }
}
