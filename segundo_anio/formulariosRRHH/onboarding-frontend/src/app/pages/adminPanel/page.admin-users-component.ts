import { Component } from '@angular/core';
import { AdminUsersListComponent } from '../../modules/adminUsers/infrastructure/components/lists/adminUsers-list.component';

@Component({
  selector: 'app-admin-users-page',
  imports: [AdminUsersListComponent],
  template: '<app-admin-users-list />',
})
export class AdminUsersPageComponent {}
