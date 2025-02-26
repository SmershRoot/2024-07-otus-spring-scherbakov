import {ChangeDetectorRef, Component, inject} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Book } from '../model/book';
import { Author} from '../model/author';
import { BookServiceService } from '../service/book-service.service';
import {FormsModule} from '@angular/forms';
import {AuthorService} from '../service/author.service';
import {Select} from 'primeng/select';
import { MultiSelectModule } from 'primeng/multiselect';
import {GenreService} from "../service/genre.service";
import {Genre} from "../model/genre";




@Component({
  selector: 'app-book-form',
  imports: [
      FormsModule,
      Select,
      MultiSelectModule
  ],
  templateUrl: './book-form.component.html',
  styleUrl: './book-form.component.css'
})
export class BookFormComponent {

  book: Book= new Book(0, '', {} as Author, []);
  authors: Author[] = [];
  genres: Genre[] = [];

  constructor(
      private route: ActivatedRoute,
      private router: Router,
      private bookService: BookServiceService,
      private authorService: AuthorService,
      private genreService: GenreService
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

      genreService.findAll().subscribe(data => {
          this.genres = data;
      })

  }


  onSubmit() {
      this.bookService.save(this.book).subscribe(result => this.gotoBookList());
    }

    gotoBookList() {
      this.router.navigate(['/books']);
    }

}
