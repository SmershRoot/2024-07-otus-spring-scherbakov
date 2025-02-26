import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Genre} from '../model/genre';

@Injectable({
  providedIn: 'root'
})
export class GenreService {
  private genreUrl: string;

  constructor(private http: HttpClient) {
    this.genreUrl = '/library-api/genre';
  }

  public findAll(): Observable<Genre[]> {
    return this.http.get<Genre[]>(this.genreUrl);
  }


}
