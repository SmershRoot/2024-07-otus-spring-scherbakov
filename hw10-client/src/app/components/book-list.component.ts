import {ChangeDetectorRef, Component, inject} from '@angular/core';
import { Book } from '../model/book';
import { BookServiceService } from '../service/book-service.service';
import { OnInit } from '@angular/core';


@Component({
  selector: 'app-book-list',
  imports: [],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.css'
})
export class BookListComponent implements OnInit {

  books: Book[] = [];
  protected cd = inject(ChangeDetectorRef)

   constructor(private bookService: BookServiceService) {}

    ngOnInit() {
      this.bookService.findAll().subscribe(data => {
        this.books = data;
      });
    }

  public deleteBook(bookId:number) {
      this.bookService.deleteById(bookId).subscribe(data => {
        this.books=this.books.filter(book => book.id !== bookId);
        // this.cd.markForCheck()
      });
    }

}
