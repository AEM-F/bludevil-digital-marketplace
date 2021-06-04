import {Injectable} from '@angular/core';

import {environment} from '../../environments/environment';
import * as Stomp from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import {ChatNotification} from '../common/chatnotification';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {AuthenticationService} from './authentication.service';
import {first, map} from 'rxjs/operators';
import {ChatMessage} from '../common/chatmessage';
import {AppToastService} from './app-toast.service';
import {RxStomp} from '@stomp/rx-stomp';
import {PaginationResponse} from '../common/paginationresponse';
import {Contact} from '../common/contact';

@Injectable({
  providedIn: 'root'
})
export class SupportChatService {
  private baseSocketUrl = environment.apiBaseUrl + '/ws';
  private messageBaseUrl = environment.apiBaseUrl + '/api/messages';
  private contactBaseUrl = environment.apiBaseUrl + '/api/contacts';
  private notificationsSubject: BehaviorSubject<ChatNotification[]>;
  public notifications: Observable<ChatNotification[]>;
  private newMessagesNrSubject: BehaviorSubject<number>;
  public newMessagesNr: Observable<number>;
  private stompClient = null;
  public isSupportChatOpen = false;
  private userId: number;
  public disabled = true;

  constructor(private http: HttpClient, private toastService: AppToastService) {
    this.notificationsSubject = new BehaviorSubject<ChatMessage[]>([]);
    this.notifications = this.notificationsSubject.asObservable();
    this.newMessagesNrSubject = new BehaviorSubject<number>(0);
    this.newMessagesNr = this.newMessagesNrSubject.asObservable();
  }

  private setConnected(connected: boolean) {
    this.disabled = !connected;
  }

  public get getNotificationsList(): ChatNotification[]{
    return this.notificationsSubject.value;
  }

  public connect(userId: number): void{
    this.userId = userId;
    const socket = new SockJS(this.baseSocketUrl);
    this.stompClient = Stomp.Stomp.over(socket);

    const service = this;
    // tslint:disable-next-line:only-arrow-functions typedef
    this.stompClient.connect({}, (frame) => {
      service.setConnected(true);
      this.countAllNewMessages().pipe(first()).subscribe();
      const notificationsRoute = `/user/${userId}/queue/messages`;
      service.stompClient.subscribe(notificationsRoute, (notif) => {
        this.onNotificationsReceived(notif);
      });
    });
  }

  onNotificationsReceived(notif): void{
    let notificationsList: ChatNotification[] = this.getNotificationsList;
    let notifObJ = JSON.parse(notif.body);
    let notification = new ChatNotification(notifObJ.messageId, notifObJ.senderId, notifObJ.senderName);
    notificationsList.push(notification);
    this.notificationsSubject.next(notificationsList);
    if (this.isSupportChatOpen === false){
      this.getMessageById(notification.messageId).subscribe(message => {
        this.toastService.show(message.senderName, message.content);
      });
    }
    this.countAllNewMessages().pipe(first()).subscribe();
  }

  public countNewMessagesFrom(senderId: number): Observable<any>{
    const endpoint = `${this.messageBaseUrl}/${senderId}/${this.userId}/count`;
    return this.http.get<any>(endpoint).pipe(map((response) => {
      const responseNr: number = response.newMessagesNr;
      return responseNr;
    }));
  }

  public countAllNewMessages(): Observable<any>{
    const endpoint = `${this.messageBaseUrl}/${this.userId}/count`;
    return this.http.get<any>(endpoint).pipe(map((response) => {
      const responseNr: number = response.allNewMessagesNr;
      this.newMessagesNrSubject.next(responseNr);
      return responseNr;
    }));
  }

  public getMessageById(messageId: number): Observable<ChatMessage>{
    const endpoint = `${this.messageBaseUrl}/${messageId}`;
    return this.http.get<ChatMessage>(endpoint);
  }

  public getAllMessagesForContact(senderId: number): Observable<ChatMessage[]>{
    const endpoint = `${this.messageBaseUrl}/${senderId}/${this.userId}`;
    return this.http.get<ChatMessage[]>(endpoint);
  }

  public getAllContactsMembers(page: number, size: number): Observable<PaginationResponse<Contact>>{
    let endpoint = `${this.contactBaseUrl}/members`;
    if (this.validateNumberValue(page) && this.validateNumberValue(size)){
      endpoint += `?page=${page}&size=${size}`;
    }
    return this.http.get<PaginationResponse<Contact>>(endpoint);
  }

  public getAllContactsAdmin(page: number, size: number): Observable<PaginationResponse<Contact>>{
    let endpoint = `${this.contactBaseUrl}/admins`;
    if (this.validateNumberValue(page) && this.validateNumberValue(size)){
      endpoint += `?page=${page}&size=${size}`;
    }
    return this.http.get<PaginationResponse<Contact>>(endpoint);
  }

  public sendMessage(senderName: string, content: string, contact: Contact): void{
    let endpoint = `/app/chat`;
    if (content.trim() !== ''){
      const chatMessage = new ChatMessage(null, null, this.userId, contact.id, senderName, contact.contactName, content, null, null);
      this.stompClient.send('/app/chat', {}, JSON.stringify(chatMessage));
    }
  }

  public disconnect(): void{
    if (this.stompClient != null){
      this.stompClient.disconnect();
      console.log('Disconnected');
    }
  }

  private validateNumberValue(value: number){
    if(value !== null && value > 0){
      return true;
    }
    else {
      return false;
    }
  }
}
