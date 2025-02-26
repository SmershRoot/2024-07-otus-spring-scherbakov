import { Author } from './author';
import { Genre } from './genre';

export class Book {
  id: number;
  title: string;
  author: Author;
  genres: Genre[];

  constructor(id: number, title: string, author: Author, genres: Genre[]){
    this.id = id;
    this.title = title;
    this.author = author;
    this.genres = genres;
    }
}
