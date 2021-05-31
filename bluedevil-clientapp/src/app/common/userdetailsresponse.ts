export class UserDetailsResponse{
  public constructor(public firstName: string,
                     public lastName: string,
                     public email: string,
                     public roles: string[],
                     public imageUrl: string) {
  }
}
