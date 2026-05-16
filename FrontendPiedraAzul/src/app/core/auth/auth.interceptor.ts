import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from '../../features/users/services/auth.service';
import { getAppEnv } from '../config/app-env';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const env = getAppEnv();
  const token = authService.accessToken();
  const isApiRequest = req.url.startsWith('/api/');
  const isAuthMeRequest = req.url.includes(`${env.API_AUTH}/getPatientByToken`);
  const isRefreshRequest = req.url.includes('/protocol/openid-connect/token')
    && req.body?.toString?.().includes('grant_type=refresh_token');
  const isLoginRequest = req.url.includes('/protocol/openid-connect/token')
    && req.body?.toString?.().includes('grant_type=password');

  const requestWithToken = isApiRequest && token && !req.headers.has('Authorization')
    ? req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      })
    : req;

  return next(requestWithToken).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status !== 401 || isRefreshRequest || isLoginRequest || isAuthMeRequest) {
        return throwError(() => error);
      }

      return authService.refreshAccessToken().pipe(
        switchMap((newAccessToken) => {
          const retriedRequest = req.clone({
            setHeaders: {
              Authorization: `Bearer ${newAccessToken}`
            }
          });

          return next(retriedRequest);
        }),
        catchError((refreshError: HttpErrorResponse) => {
          authService.clearSession();
          router.navigate(['/login']);
          return throwError(() => refreshError);
        })
      );
    })
  );
};