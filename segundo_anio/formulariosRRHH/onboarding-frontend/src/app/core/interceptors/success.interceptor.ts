import { HttpInterceptorFn } from '@angular/common/http';

export const successInterceptor: HttpInterceptorFn = (req, next) => next(req);
