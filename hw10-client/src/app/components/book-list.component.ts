import {ChangeDetectorRef, Component, inject} from '@angular/core';
import { Book } from '../model/book';
import { BookServiceService } from '../service/book-service.service';
import { OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-book-list',
  imports: [],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.css'
})
export class BookListComponent implements OnInit {


  books: Book[] = [];
  protected cd = inject(ChangeDetectorRef)

  constructor(
    private bookService: BookServiceService,
    private router: Router
  ) {}

  ngOnInit() {
      this.bookService.findAll().subscribe(data => {
        this.books = data;
      });
  }

  public editBook(bookId:number) {
    this.router.navigate(['/book/', bookId]);
  }

  public deleteBook(bookId:number) {
      this.bookService.deleteById(bookId).subscribe(data => {
        this.books=this.books.filter(book => book.id !== bookId);
        // this.cd.markForCheck()
      });
    }

}
