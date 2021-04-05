import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  private baseUrl = environment.apiBaseUrl + '/api/images';

  constructor(private http: HttpClient) { }

  uploadProductImage(fileToUpload: File, productId: number): Observable<any> {
    const endpoint = `${this.baseUrl}/upload/product/${productId}`;
    const formData: FormData = new FormData();
    formData.append('file', fileToUpload, fileToUpload.name);
    return this.http.post(endpoint, formData);
  }
}
