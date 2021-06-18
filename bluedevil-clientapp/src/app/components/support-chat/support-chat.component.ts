import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {SupportChatService} from '../../services/support-chat.service';
import {ChatNotification} from '../../common/chatnotification';
import {Contact} from '../../common/contact';
import {AuthenticationService} from '../../services/authentication.service';
import {animate, style, transition, trigger} from '@angular/animations';
import {ChatMessage} from '../../common/chatmessage';
import {AppToastService} from '../../services/app-toast.service';
import {Router} from '@angular/router';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {DOCUMENT} from '@angular/common';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-support-chat',
  templateUrl: './support-chat.component.html',
  styleUrls: ['./support-chat.component.css'],
  animations: [
    trigger(
      'inOutAnimation',
      [
        transition(
          ':enter',
          [
            style({ opacity: 0 }),
            animate('1s ease-out',
              style({ opacity: 1 }))
          ]
        ),
        transition(
          ':leave',
          [
            style({ opacity: 1 }),
            animate('1s ease-in',
              style({ opacity: 0 }))
          ]
        )
      ]
    )
  ]
})
export class SupportChatComponent implements OnInit, OnDestroy {
  chatForm: FormGroup;
  isLoadingContacts = false;
  pageNr = 1;
  pageSize = 5;
  totalElements = 0;
  contacts: Contact[] = [];
  contactError;
  selectedContact: Contact = null;
  currentMessages: ChatMessage[] = null;
  userId: number = null;
  userName: string = null;
  userImagePath: string = '';
  isLoadingMessages = false;
  notificationSubscription: Subscription;
  detailsError = null;
  isDeletingChat = false;
  constructor(private sChatService: SupportChatService,
              private authService: AuthenticationService,
              private toastService: AppToastService,
              private router: Router,
              private formBuilder: FormBuilder,
              @Inject(DOCUMENT) private document: Document) {
    this.chatForm = this.formBuilder.group({
      inputMessage : new FormControl('', [Validators.required])
    });
  }

  ngOnInit(): void {
    let userJwt = this.authService.getUserValue;
    if (userJwt !== null && userJwt !== undefined){
      this.authService.getUserInfo().subscribe(response => {
        if ((response.firstName !== '' && response.firstName !== null) || (response.lastName !== '' && response.lastName !== null)){
          this.userImagePath = this.checkProfileImage(response.imageUrl);
          this.userName = response.firstName;
          this.userId = userJwt.getId;
          this.sChatService.isSupportChatOpen = true;
          this.getContacts();
          this.notificationSubscription = this.sChatService.notifications.subscribe((notifications) => {
            if (notifications !== null && notifications !== undefined && notifications.length > 0){
              const lastNotification = notifications[notifications.length - 1];
              if (lastNotification !== undefined){
                this.handleNewNotification(lastNotification.senderId, lastNotification);
              }
            }
          });
        }
        else{
          this.detailsError = 'First-name and last-name not provided, please go to your profile and fill them out!';
        }
      });
    } else{
      this.router.navigate(['/login']);
    }
  }

  checkRole(roleName: string): boolean{
      const roles: string[] = this.authService.getUserRoles;
      for (const role of roles){
        if (role === roleName){
          return true;
        }
      }
      return false;
  }

  deleteChatForSelectedUser(){
    if (this.selectedContact && this.currentMessages.length > 0){
      this.isDeletingChat = true;
      const contactName = this.selectedContact.contactName;
      this.sChatService.deleteChat(this.selectedContact).subscribe(next => {
        this.isDeletingChat = false;
        this.currentMessages = [];
        const successMsg = `Successfully deleted the chat with member ${contactName}`;
        this.toastService.show('System:', successMsg);
      }, error => {
        this.isDeletingChat = false;
        const errorMsg = error.error.message || error.message;
        const failMsg = `Failed to delete the chat with ${contactName}, reason: ${errorMsg}`;
        this.toastService.show('System:', failMsg);
      });
    }
  }

  onErrorNavBtn(){
    this.detailsError = null;
    this.router.navigate(['/account']);
  }

  getImagePathFromSender(senderId: number): string{
    let contact: Contact = null;
    contact = this.contacts.find(obj => obj.id === senderId);
    if (contact !== null && contact !== undefined){
      return this.checkProfileImage(contact.contactProfileImage);
    }else{
      return this.checkProfileImage(this.userImagePath);
    }
  }

  handleNewNotification(contactId: number, notification: ChatNotification): void{
    let contact: Contact = null;
    contact = this.contacts.find(obj => obj.id === contactId);
    if (contact !== null){
      if (this.selectedContact != null){
        if (contact.id !== this.selectedContact.id){
          const itemIndex = this.contacts.findIndex(item => item.id === contact.id);
          this.contacts[itemIndex].notificationNr += 1;
        }else{
          this.sChatService.getMessageById(notification.messageId).subscribe(message => {
            if (this.currentMessages === null){
              this.currentMessages = [];
            }
            message.timestamp = this.convertUTCToLocalDateIgnoringTimezone(new Date(message.timestamp + "Z"))
            this.currentMessages.push(message);
            setTimeout(() => {this.scrollToMessage(message.messageId); }, 500);
          });
        }
      }else{
        const itemIndex = this.contacts.findIndex(item => item.id === contact.id);
        if (this.contacts[itemIndex] !== undefined){
          this.contacts[itemIndex].notificationNr += 1;
        }
      }
    }else{
      this.sChatService.getMessageById(notification.messageId).subscribe(returnedMessage => {
        this.toastService.show(returnedMessage.senderName, returnedMessage.content);
      });
    }
  }

  getContacts(): void{
    this.isLoadingContacts = true;
    const roles: string[] = this.authService.getUserRoles;
    if (roles.includes('ADM')){
      this.sChatService.getAllContactsAdmin(this.pageNr, this.pageSize).subscribe(this.processContacts(), this.processContactsError());
    }
    else if (roles.includes('MEM')){
      this.sChatService.getAllContactsMembers(this.pageNr, this.pageSize).subscribe(this.processContacts(), this.processContactsError());
    }
  }

  processContacts(){
    return responseBody => {
      let contactList: Contact[] = [];
      for (const contact of responseBody.objectsList){
        const contactApp = new Contact(contact.id, contact.contactName, contact.contactProfileImage, 0);
        contactList.push(contactApp);
      }
      this.contacts = contactList;
      this.pageNr = responseBody.pageNumber;
      this.pageSize = responseBody.pageSize;
      this.totalElements = responseBody.totalElements;
      this.isLoadingContacts = false;
      this.countNotifications();
    };
  }

  countNotifications(): void{
    for (const contact of this.contacts){
      this.sChatService.countNewMessagesFrom(contact.id).subscribe( returnedNr => {
        contact.notificationNr = returnedNr;
      });
    }
  }

  processContactsError(){
    return error => {
      this.pageSize = 5;
      this.pageNr = 1;
      this.contacts = [];
      this.contactError = error.error.message || error.message;
      this.isLoadingContacts = false;
    };
  }

  getChatFromContact(contactId: number){
    this.isLoadingMessages = true;
    this.sChatService.getAllMessagesForContact(contactId).subscribe(chatMessages => {
      this.isLoadingMessages = false;
      for (let message of chatMessages){
        message.timestamp = this.convertUTCToLocalDateIgnoringTimezone(new Date(message.timestamp + "Z"));
      }
      this.currentMessages = chatMessages;
      setTimeout(() => {
        this.scrollToTheBottomOfTheChat();
      }, 500);
      this.countNotifications();
    });
  }

  handleRefresh(){
    this.pageNr = 1;
    this.isLoadingContacts = false;
    this.contactError = null;
    this.contacts = [];
    this.getContacts();
  }

  checkProfileImage(imageUrl: string): string{
    if (imageUrl === null || imageUrl === undefined || imageUrl === ''){
      imageUrl = '../../../assets/images/default-p-img.png';
    }
    return imageUrl;
  }

  checkIfContactIsSelected(id: number): boolean{
    if (this.selectedContact == null){
      return false;
    }
    else{
      return this.selectedContact.id === id;
    }
  }

  handleContactSelect(contact: Contact): void{
    if ((this.selectedContact != null) && (contact.id === this.selectedContact.id)){
      this.selectedContact = null;
      this.currentMessages = null;
      this.countNotifications();
     // console.log(this.selectedContact);
    }
    else{
      this.selectedContact = contact;
     // console.log(this.selectedContact);
      this.getChatFromContact(this.selectedContact.id);
    }
  }

  onMessageSubmit(): void{
    if (this.userName !== null && this.selectedContact !== null && this.chatForm.controls.inputMessage.value !== ''){
      this.sChatService.sendMessage(this.userName, this.chatForm.controls.inputMessage.value, this.selectedContact);
      const chatMessage = new ChatMessage(
        null,
        null,
        this.userId,
        this.selectedContact.id,
        this.userName,
        this.selectedContact.contactName,
        this.chatForm.controls.inputMessage.value,
        new Date(),
        null);
      if (this.currentMessages === null){
        this.currentMessages = [];
      }
     // chatMessage.timestamp = this.convertUTCToLocalDateIgnoringTimezone(new Date(chatMessage.timestamp + "Z"));
      this.currentMessages.push(chatMessage);
      this.chatForm.reset();
      setTimeout(() => {this.scrollToLastElementOfTheChat(); }, 500);
      // this.sChatService.getAllMessagesForContact(this.selectedContact.id).subscribe( response => {
      //   if (this.currentMessages === null){
      //     this.currentMessages = [];
      //   }
      //   let message = response[response.length - 1];
      //   message.timestamp = this.convertUTCToLocalDateIgnoringTimezone(new Date(message.timestamp + "Z"));
      //   this.currentMessages.push(message);
      //   this.chatForm.reset();
      //   setTimeout(() => {this.scrollToMessage(message.messageId); }, 500);
      // });
    }
  }

  scrollToTheBottomOfTheChat(){
    let chatWindow: HTMLElement = this.document.getElementById('chat-window');
    chatWindow.scrollTop = chatWindow.scrollHeight;
  }

  scrollToLastElementOfTheChat(){
    let chatWindow: HTMLElement = this.document.getElementById('chat-window');
    let lastMessage = chatWindow.lastElementChild;
    let messagePos = (lastMessage as HTMLElement).offsetTop;
    chatWindow.scrollTop = messagePos;
  }

  scrollToMessage(messageId: number){
    let messageDomId = 'msg-' + messageId;
    let chatMessage: HTMLElement = this.document.getElementById(messageDomId);
    let messagePos = chatMessage.offsetTop;
    let chatWindow: HTMLElement = this.document.getElementById('chat-window');
    chatWindow.scrollTop = messagePos;
  }

  ngOnDestroy(): void {
    this.sChatService.isSupportChatOpen = false;
    if (this.notificationSubscription !== undefined){
      this.notificationSubscription.unsubscribe();
    }
  }

   convertUTCToLocalDateIgnoringTimezone(utcDate: Date): Date{
    return new Date(
      utcDate.getUTCFullYear(),
      utcDate.getUTCMonth(),
      utcDate.getUTCDate(),
      utcDate.getUTCHours(),
      utcDate.getUTCMinutes(),
      utcDate.getUTCSeconds(),
      utcDate.getUTCMilliseconds(),
    );
  }

}
