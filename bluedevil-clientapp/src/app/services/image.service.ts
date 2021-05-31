import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {AuthenticationService} from './authentication.service';
import {UpdateImageUrlRequest} from '../common/updateimageurlrequest';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  private baseUrl = environment.apiBaseUrl + '/api/images';

  constructor(private http: HttpClient, private authService: AuthenticationService) { }

  uploadProductImage(fileToUpload: File, productId: number): Observable<any> {
    const endpoint = `${this.baseUrl}/upload/product/${productId}`;
    const formData: FormData = new FormData();
    formData.append('file', fileToUpload, fileToUpload.name);
    return this.http.post(endpoint, formData);
  }

  uploadUserImage(fileToUpload: File): Observable<any>{
    const refreshToken = this.authService.getUserValue.getRefreshToken;
    const endpoint = `${this.baseUrl}/upload/user/${refreshToken}`;
    const formData: FormData = new FormData();
    formData.append('file', fileToUpload, fileToUpload.name);
    return this.http.post(endpoint, formData);
  }

  updateUserImageUrl(imageUrl: string): Observable<any>{
    const refreshToken = this.authService.getUserValue.getRefreshToken;
    const endpoint = `${this.baseUrl}/update/user`;
    const requestBody: UpdateImageUrlRequest = new UpdateImageUrlRequest(refreshToken, imageUrl);
    return this.http.post(endpoint, requestBody);
  }
}
