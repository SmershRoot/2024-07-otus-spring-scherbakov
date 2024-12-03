import { Component } from '@angular/core';
import {RouterOutlet, RouterLink, RouterModule} from '@angular/router';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  imports: [RouterModule, CommonModule,RouterOutlet, RouterLink],
  standalone: true,
})
export class AppComponent {
  title = 'library-project';
}
