import { Component } from '@angular/core';
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

   constructor(private bookService: BookServiceService) {}

    ngOnInit() {
      this.bookService.findAll().subscribe(data => {
        this.books = data;
      });
    }

}
