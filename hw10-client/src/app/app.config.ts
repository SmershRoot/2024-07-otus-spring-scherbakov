import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { BookListComponent } from './components/book-list.component';
import { BookFormComponent } from './components/book-form.component';
import {BookServiceService} from './service/book-service.service';
import {provideHttpClient} from '@angular/common/http';

import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {providePrimeNG} from 'primeng/config';
import Aura from '@primeng/themes/aura';

const routes = [
  { path: 'books', component: BookListComponent },
  { path: 'book', children:[{path: ':id', component: BookFormComponent}]}
];

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    BookServiceService,
    provideHttpClient(),
    provideAnimationsAsync(),
    providePrimeNG({
      theme: {
        preset: Aura
      }
    })
  ],
};
