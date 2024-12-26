import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Book } from '../model/book';
import { Observable } from 'rxjs';

@Injectable()
export class BookServiceService {
  private bookUrl: string;

  constructor(private http: HttpClient) {
      this.bookUrl = '/library-api/book';
  }

    public findAll(): Observable<Book[]> {
    return this.http.get<Book[]>(this.bookUrl);
  }

  public save(book: Book) {
    return this.http.post<Book>(this.bookUrl, book);
  }

}
