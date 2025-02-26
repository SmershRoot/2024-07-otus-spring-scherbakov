import {ChangeDetectorRef, Component, inject} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Book } from '../model/book';
import { Author} from '../model/author';
import { BookServiceService } from '../service/book-service.service';
import {FormsModule} from '@angular/forms';
import {AuthorService} from '../service/author.service';
import {Select} from 'primeng/select';


@Component({
  selector: 'app-book-form',
  imports: [
    FormsModule,
    Select
  ],
  templateUrl: './book-form.component.html',
  styleUrl: './book-form.component.css'
})
export class BookFormComponent {

  book: Book= new Book(0, '', {} as Author, []);
  authors: Author[] = [];
  authorsOptions!:Author[];

  constructor(
      private route: ActivatedRoute,
      private router: Router,
      private bookService: BookServiceService,
      private authorService: AuthorService
  ) {
        let bookId = this.route.snapshot.params['id'];
        if (bookId !== "new") {
          bookService.findById(bookId).subscribe(book => {
            this.book = book;
          })
        }

        authorService.findAll().subscribe(data => {
          this.authors = data;
        });
        console.log("TEST: " + this.book.title);
        console.log("TEST: " + this.authors.length);
  }


  onSubmit() {
      this.bookService.save(this.book).subscribe(result => this.gotoBookList());
    }

    gotoBookList() {
      this.router.navigate(['/books']);
    }

}
