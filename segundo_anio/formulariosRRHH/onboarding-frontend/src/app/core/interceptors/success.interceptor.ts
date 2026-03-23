import { HttpInterceptorFn } from '@angular/common/http';

// Success notifications are handled per-service via ToastService
export const successInterceptor: HttpInterceptorFn = (req, next) => next(req);
