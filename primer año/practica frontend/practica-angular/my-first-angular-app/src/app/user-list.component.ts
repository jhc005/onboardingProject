import { Component} from "@angular/core";
import { OnInit } from "@angular/core";
import { UserCardComponent } from "./user-card.component";

interface User {
  id: number;
  name: string;
  address: {
    city: string;
    zipcode: string;
  };
}

@Component({
  selector: "app-user-list",
  imports: [UserCardComponent],
  template: `
   <div class="grid-container">
        @for(user of users; track user.id){
        <app-user-card id="{{user.id}}" name="{{user.name}}" city="{{user.address.city}}" zipcode="{{user.address.zipcode}}"></app-user-card>
  }
   </div>
   `,
  styles: [`
.grid-container {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-template-rows: repeat(2, auto);
  gap: 20px;
  padding: 40px;
  padding-left: 60px;
    }
  @media screen and (max-width:1200px){
  .grid-container{
    grid-template-columns: repeat(3,1fr);
  }
}
  @media screen and (max-width:900px){
  .grid-container{
    grid-template-columns: repeat(2,1fr);
  }
}
  @media screen and (max-width:600px){
  .grid-container{
    grid-template-columns: repeat(1,1fr);
  }
}
    `],
})

export class UserListComponent implements OnInit {
    UsersNumbers = 7;
    users: User[] = [];

  ngOnInit() {
    this.fetchUsers();
  }

  fetchUsers() {
    fetch("https://jsonplaceholder.typicode.com/users")
      .then((response) => response.json())
      .then((data: User[]) => {
        this.users = data.slice(0, this.UsersNumbers);
        console.log(this.users);
      })
      .catch((error) => console.error("Error fetching users:", error));
  }
}