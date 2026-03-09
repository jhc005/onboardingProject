import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {

  const toastr = inject(ToastrService);

  return next(req).pipe(

    catchError((error: HttpErrorResponse) => {

      let message = 'Error inesperado';

      if (error.error?.message) {
        message = error.error.message;
      } 
      else if (error.status === 0) {
        message = 'No se pudo conectar con el servidor';
      } 
      else if (error.status === 404) {
        message = 'Recurso no encontrado';
      } 
      else if (error.status === 500) {
        message = 'Error interno del servidor';
      }

      toastr.error(message, 'Error');

      return throwError(() => error);

    })

  );

};