import { Component } from '@angular/core';
import {AuthenticationService} from './services/authentication.service';
import {SupportChatService} from './services/support-chat.service';
import {AppToastService} from './services/app-toast.service';
import {first} from 'rxjs/operators';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'bluedevil-clientapp';
  isSideNavOpen = false;
  user;
  newMessageNumber = 0;
  countNewMessagesSubscription: Subscription;


  constructor(private authenticationService: AuthenticationService,
              private sChatService: SupportChatService,
              public toastService: AppToastService) {
    this.authenticationService.user.subscribe(res => {
      this.user = res;
    });
    this.sChatService.newMessagesNr.subscribe((newMessagesNr) => {
      if (this.sChatService.isSupportChatOpen === false){
        this.newMessageNumber = newMessagesNr;
      }
      });
  }

  checkForNewMessages(): void{
    if (this.sChatService.disabled === false){
      this.countNewMessagesSubscription= this.sChatService.countAllNewMessages().pipe(first()).subscribe(next =>{},error => {
        this.countNewMessagesSubscription.unsubscribe();
        this.checkForNewMessages();
      });
    }else{
      console.log('Connection not established to socket -> no new messages');
    }
  }

  onSideNavOpen(){
    this.isSideNavOpen = true;
    this.checkForNewMessages();
  }

  onSideNavClose(){
    this.isSideNavOpen = false;
  }

  logoutUser(): void{
    this.authenticationService.logout();
  }

  checkRole(roleName: string): boolean{
    if (this.user != null){
      const roles: string[] = this.authenticationService.getUserRoles;
      for (const role of roles){
        if (role === roleName){
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
