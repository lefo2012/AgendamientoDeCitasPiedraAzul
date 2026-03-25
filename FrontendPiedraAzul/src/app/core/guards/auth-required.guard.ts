import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../../features/users/services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

const LOGIN_REQUIRED_MESSAGE = 'Debes iniciar sesion para agendar una cita.';

export const authRequiredGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const snackBar = inject(MatSnackBar);

  if (authService.isAuthenticated()) {
    return true;
  }

  snackBar.open(LOGIN_REQUIRED_MESSAGE, 'Ir al login', {
    duration: 4200,
    horizontalPosition: 'end',
    verticalPosition: 'top',
    panelClass: ['auth-warning-snackbar']
  });

  return router.createUrlTree(['/login'], {
    queryParams: { message: LOGIN_REQUIRED_MESSAGE }
  });
};