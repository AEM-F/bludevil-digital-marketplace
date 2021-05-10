import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Genre} from '../common/genre';
import {map} from 'rxjs/operators';
import {environment} from '../../environments/environment';
import {PaginationResponse} from '../common/paginationresponse';

@Injectable({
  providedIn: 'root'
})
export class GenreService {
  private baseUrl = environment.apiBaseUrl + '/api/genres';
  constructor(private http: HttpClient) { }

  getGenreList(retrieval:string, page: number, size: number): Observable<any>{
    let searchUrl = `${this.baseUrl}?retrieval=${retrieval}`;
    if (retrieval.toLowerCase() === 'page'){
      searchUrl += `&page=${page}&size=${size}`;
    }
    return this.http.get<any>(searchUrl).pipe(
      map(response => {
        return response;
      })
    );
  }

  getGenreByName(name: string): Observable<any>{
    const searchUrl = `${this.baseUrl}/getByName/${name}`;
    return this.http.get(searchUrl);
  }

  updateGenre(genre: Genre, genreId: number): Observable<any>{
    const endpoint = `${this.baseUrl}/${genreId}`;
    return this.http.put(endpoint, genre);
  }

  createGenre(genre: Genre): Observable<any>{
    return this.http.post(this.baseUrl, genre);
  }

  checkNameValidity(genreName: string): Observable<any>{
    genreName = genreName.toLowerCase();
    const endpoint = `${this.baseUrl}/checkNameValidity/${genreName}`;
    return this.http.get(endpoint).pipe(
      map((data: any) => {
        return data;
      })
    );
  }

  searchGenresPaginate(page: number, size: number, keyword: string): Observable<PaginationResponse<Genre>> {
    let searchUrl = `${this.baseUrl}/search/${keyword}`;
    searchUrl += `?page=${page}&size=${size}`;
    return this.http.get<PaginationResponse<Genre>>(searchUrl).pipe(
      map(response => {
        return response;
      })
    );
  }
}
