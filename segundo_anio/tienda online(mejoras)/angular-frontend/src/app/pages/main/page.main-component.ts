import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

interface MenuItem {
  title: string;
  description: string;
  path: string;
  icon: string;
}

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './page.main-component.html',
  styleUrls: ['./page.main-component.css']
})
export class MainPageComponent {

  constructor(private router: Router) { }

  menuItems: MenuItem[] = []; 

  ngOnInit() {
    this.menuItems = [
      { title: 'Products', description: 'Manage store products', path: '/products', icon: '📦' },
      { title: 'Customers', description: 'Customer management', path: '/customers', icon: '👥' },
      { title: 'Orders', description: 'View and manage orders', path: '/orders', icon: '🧾' },
      { title: 'Categories', description: 'View and manage all categories', path: '/categories', icon: '📋​' }
    ];
  }

  navigate(path: string) {
    this.router.navigate([path]);
  }
}