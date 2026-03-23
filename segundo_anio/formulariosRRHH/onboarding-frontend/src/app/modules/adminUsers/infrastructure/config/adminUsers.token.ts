import { InjectionToken } from '@angular/core';
import { IAdminUsersRepository } from '../../domain/adminUsers.repository';

export const ADMIN_USERS_REPOSITORY = new InjectionToken<IAdminUsersRepository>(
  'IAdminUsersRepository'
);
