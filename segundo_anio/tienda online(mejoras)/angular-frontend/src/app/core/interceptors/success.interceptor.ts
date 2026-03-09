import { HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { tap } from 'rxjs';

export const successInterceptor: HttpInterceptorFn = (req, next) => {

  const toastr = inject(ToastrService);

  return next(req).pipe(

    tap(event => {

      if (event instanceof HttpResponse) {

        switch (req.method) {

          case 'POST':
            toastr.success('Creado correctamente', 'Éxito');
            break;

          case 'PUT':
          case 'PATCH':
            toastr.success('Actualizado correctamente', 'Éxito');
            break;

          case 'DELETE':
            toastr.success('Eliminado correctamente', 'Éxito');
            break;

        }

      }

    })

  );

};