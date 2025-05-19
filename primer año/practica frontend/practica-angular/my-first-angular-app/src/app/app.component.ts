import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { UserListComponent } from './user-list.component';

@Component({
  selector: 'app-root',
  imports: [UserListComponent],
  template: `
  <h1><span class="dot"></span>Usuarios</h1>
  <app-user-list/>`,
  styles: [`
    h1{
      color: white;
      font-size: 2rem;
      text-align: right;
      margin: 0;
      padding: 0;
      margin-right: 20px;
    }
    
    .dot {
      display: inline-block;
      width: 15px;
      height: 15px;
      background-color: rgb(255, 80, 56);
      border-radius: 50%;
      margin-right: 10px;
    }`]
})
export class AppComponent {
  title = 'my-first-angular-app';
}
