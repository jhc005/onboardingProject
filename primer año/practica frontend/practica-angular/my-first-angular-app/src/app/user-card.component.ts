import { Component, Input } from "@angular/core";

@Component({
  selector: "app-user-card",
  template:`
    <div class="container">
        <div class="name-section">
          <div class="user-icon">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512" class="user-svg">
              <path d="M224 256A128 128 0 1 0 224 0a128 128 0 1 0 0 256zm-45.7 48C79.8 304 0 383.8 0 482.3C0 498.7 13.3 512 29.7 512H418.3c16.4 0 29.7-13.3 29.7-29.7C448 383.8 368.2 304 269.7 304H178.3z"/>
            </svg>
          </div>
          <h1>{{name}}</h1>
        </div>
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

.name-section {
  display: flex;
  align-items: center;
  gap: 15px;
  padding-left: 10px;
}

.user-icon {
  display: flex;
  align-items: center;
}

.user-svg {
  width: 24px;
  height: 24px;
  fill: rgb(255, 80, 56);
  transition: fill 0.3s ease;
}

.container:hover .user-svg {
  fill: white;
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
}
`],
})
export class UserCardComponent {
    @Input() id = '';
    @Input() name = '';
    @Input() city = '';
    @Input() zipcode = '';
}