import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Genre} from '../common/genre';
import {map} from 'rxjs/operators';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GenreService {
  private baseUrl = environment.apiBaseUrl + '/api/genres';
  constructor(private http: HttpClient) { }

  getGenreList(): Observable<Genre[]>{
    const searchUrl = `${this.baseUrl}?retrieval=all`;
    return this.http.get<Genre[]>(searchUrl).pipe(
      map(response => {
        return response;
      })
    );
  }
}
