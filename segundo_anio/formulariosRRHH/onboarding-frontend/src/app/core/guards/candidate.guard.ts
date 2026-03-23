import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';

export const candidateGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const router = inject(Router);
  const token = route.queryParamMap.get('token');

  if (token) return true;
  return router.createUrlTree(['/login']);
};
