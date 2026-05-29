import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { catchError, map, of } from 'rxjs';
import { AuthService } from '../../features/users/services/auth.service';
import { NoPermissionDialog } from '../../shared/dialogs/no-permission-dialog/no-permission-dialog';

const NO_PERMISSION_MESSAGE = 'Tu cuenta no tiene permisos para gestionar citas.';

export const canScheduleGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const dialog = inject(MatDialog);

  if (authService.canScheduleAppointments()) {
    return true;
  }

  if (authService.isAuthenticated()) {
    dialog.open(NoPermissionDialog, {
      width: '420px',
      data: { message: NO_PERMISSION_MESSAGE }
    });

    return router.createUrlTree(['/citas/agendar']);
  }

  return authService.restoreSession().pipe(
    map((doctor) => {
      if (doctor?.canSchedule === true) {
        return true;
      }

      dialog.open(NoPermissionDialog, {
        width: '420px',
        data: { message: NO_PERMISSION_MESSAGE }
      });

      return router.createUrlTree(['/citas/agendar']);
    }),
    catchError(() => {
      dialog.open(NoPermissionDialog, {
        width: '420px',
        data: { message: NO_PERMISSION_MESSAGE }
      });

      return of(router.createUrlTree(['/citas/agendar']));
    })
  );
};
