import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { BookListComponent } from './components/book-list.component';
import { BookFormComponent } from './components/book-form.component';
import { AppComponent } from './app.component';
import {BookServiceService} from './service/book-service.service';
import {provideHttpClient} from '@angular/common/http';

const routes = [
  { path: 'books', component: BookListComponent },
  { path: 'addbook', component: BookFormComponent }
];

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes), BookServiceService, provideHttpClient()],
};