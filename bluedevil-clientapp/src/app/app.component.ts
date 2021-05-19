import { Component } from '@angular/core';
import {AuthenticationService} from './services/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'bluedevil-clientapp';
  isSideNavOpen = false;
  user;


  constructor(private authenticationService: AuthenticationService) {
    this.authenticationService.user.subscribe(res => {
      this.user = res;
    });
  }

  onSideNavOpen(){
    this.isSideNavOpen = true;
  }

  onSideNavClose(){
    this.isSideNavOpen = false;
  }

  logoutUser(): void{
    this.authenticationService.logout();
  }

  checkAdmin(): boolean{
    if (this.user != null){
      const roles: string[] = this.authenticationService.getUserRoles;
      for (const role of roles){
        if (role === 'ADM'){
          return true;
        }
      }
      return false;
    }
    else{
      return false;
    }
  }
}
