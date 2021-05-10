export class UserDetailsResponse{
  public constructor(private firstName: string,
                     private lastName: string,
                     private email: string,
                     private roles: string[],
                     private imagePath: string) {
  }


  get getFirstName(): string {
    return this.firstName;
  }

  set setFirstName(value: string) {
    this.firstName = value;
  }

  get getLastName(): string {
    return this.lastName;
  }

  set setLastName(value: string) {
    this.lastName = value;
  }

  get getEmail(): string {
    return this.email;
  }

  set setEmail(value: string) {
    this.email = value;
  }

  get getRoles(): string[] {
    return this.roles;
  }

  set setRoles(value: string[]) {
    this.roles = value;
  }

  get getImagePath(): string {
    return this.imagePath;
  }

  set setImagePath(value: string) {
    this.imagePath = value;
  }
}
