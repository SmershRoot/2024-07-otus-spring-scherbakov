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

  public findById(id: number): Observable<Book> {
    return this.http.get<Book>(this.bookUrl + '/' + id);
  }

  public save(book: Book) {
    if (book.id === 0) {
      return this.http.post<Book>(this.bookUrl, book);
    } else {
      return this.http.put<Book>(this.bookUrl + '/' + book.id, book);
    }
  }

  public deleteById(id: number) {
    return this.http.delete<Book>(this.bookUrl + '/' + id);
  }

}
