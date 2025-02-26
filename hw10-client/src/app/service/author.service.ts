import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Author} from '../model/author';

@Injectable({
  providedIn: 'root'
})
export class AuthorService {
  private authorUrl: string;

  constructor(private http: HttpClient) {
    this.authorUrl = '/library-api/author';
  }

  public findAll(): Observable<Author[]> {
    return this.http.get<Author[]>(this.authorUrl);
  }

}
