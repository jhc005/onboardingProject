import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { ToastService } from '../services/toast.service';
import { AuthService } from '../services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const toast = inject(ToastService);
  const auth = inject(AuthService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((error) => {
      const message = error.error?.message || 'Ha ocurrido un error inesperado';

      if (error.status === 401) {
        auth.logout();
        router.navigate(['/login']);
        toast.error('Sesión expirada. Inicia sesión de nuevo.');
      } else if (error.status === 403) {
        toast.error('No tienes permisos para esta acción.');
      } else if (error.status === 0) {
        toast.error('No se puede conectar con el servidor.');
      } else {
        toast.error(message);
      }

      return throwError(() => error);
    })
  );
};
