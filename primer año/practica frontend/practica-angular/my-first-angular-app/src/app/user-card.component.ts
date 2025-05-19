import { Component, Input } from "@angular/core";

@Component({
  selector: "app-user-card",
  template:`
    <div class="container">
        <h1>{{name}}</h1>
        <p>Ciudad : {{city}}</p>
        <p>Codigo Postal : {{zipcode}}</p>
        <button class="arrow-button">➜</button>
    </div>`,
  styles: [`
.container {
  position: relative;
  background-color: #1d1d1d;
  border-radius: 30px;
  padding: 10px;
  margin: 10px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  width: 300px;
  height: 300px;
  align-content: center;
  transition:  background-color 2s ease;
  overflow: hidden;
}
.container::before{
  content: "";
  position: absolute;
  inset: 0;
  background: radial-gradient(circle at bottom right, rgba(255,80,56,255), transparent 50%);
  opacity: 0;
  transition: opacity 1s ease;
}
.container:hover::before {
  opacity: 1;
}
.container h1 {
  margin-top: 0;
  color: white;
  font-size: 1.5rem;
}

.container p{
  margin-bottom: 0;
  color: white;
  margin-bottom: 1vw;
}
button {
  border-radius: 8px;
  border: 1px solid transparent;
  padding: 0.6em 1.2em;
  font-size: 1em;
  background-color: grey;
  cursor: pointer;
  transition: background-color 1s;
}
.container button{
    width: 60px;
    height: 50px;
    align-items: center;
    display: flex;
}
.container .arrow-button{
    color: white;
}
.container:hover .arrow-button{
  color: black;
  background-color: white;
}`],
})
export class UserCardComponent {
    @Input() id = '';
    @Input() name = '';
    @Input() city = '';
    @Input() zipcode = '';
}