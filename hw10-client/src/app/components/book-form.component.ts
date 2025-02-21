import {ChangeDetectorRef, Component, inject} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Book } from '../model/book';
import { Author} from '../model/author';
import { BookServiceService } from '../service/book-service.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-book-form',
  imports: [
    FormsModule
  ],
  templateUrl: './book-form.component.html',
  styleUrl: './book-form.component.css'
})
export class BookFormComponent {

  book: Book = new Book(0, '', {} as Author, []);

  constructor(
      private route: ActivatedRoute,
        private router: Router,
          private bookService: BookServiceService) {
            console.log(this.book);
            this.book = new Book(0, '', {} as Author, []);
    }


  onSubmit() {
      this.bookService.save(this.book).subscribe(result => this.gotoBookList());
    }

    gotoBookList() {
      this.router.navigate(['/books']);
    }

}
