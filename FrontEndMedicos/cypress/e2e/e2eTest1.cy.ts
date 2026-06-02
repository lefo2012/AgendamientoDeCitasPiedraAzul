describe('Front Médicos — Flujo completo', () => {
  const loginAsDoctor = () => {
    cy.visit('/login');
    cy.get('input[formcontrolname="email"]').should('be.visible').clear().type('nelson@gmail.com');
    cy.get('input[formcontrolname="password"]').should('be.visible').clear().type('12345678');
    cy.contains('button', 'Ingresar').click();
  };

  it('Médico puede iniciar sesión y ver el reporte de citas', () => {
    loginAsDoctor();

    cy.contains('h1', 'Ingresar al panel principal').click();
    //cy.visit('/citas/agendar');

    cy.contains('h2', 'Reporte de Citas').should('be.visible');
    cy.contains('Filtra las citas por doctor o fecha').should('be.visible');
    cy.get('.appointment-table').should('be.visible');
  });

  it('Médico puede agendar una nueva cita', () => {
    loginAsDoctor();

    cy.contains('h1', 'Ingresar al panel principal').click();
    cy.contains('button', 'Nueva').click();

    cy.contains('Nueva Cita').should('be.visible');

    // Paciente — escribe el número, espera el autocomplete y selecciona el primero
    cy.get('input[formcontrolname="identificationNumber"]')
      .should('be.visible')
      .clear()
      .type('1111111111');

    // Espera a que el panel del autocomplete aparezca con resultados
    cy.get('mat-option').not('[disabled]').first().click();

    // 1. Abre el mat-select
    cy.get('mat-select[formcontrolname="doctorId"]').click();

    // 2. Espera a que el overlay esté visible y selecciona la opción
    cy.get('mat-option').first().click();

    cy.wait(1500);

    // Fecha
    cy.get('mat-datepicker-toggle').click();
    cy.get('.mat-calendar').should('be.visible');
    cy.get('.mat-calendar-body-cell-container button.mat-calendar-body-cell')
      .not('.mat-calendar-body-disabled')
      .first()
      .click({ force: true }); // fuerza el click ignorando overlays

    // Cierra el calendario presionando Escape
    cy.get('body').type('{esc}');

    // Espera que el backdrop desaparezca
    cy.get('.cdk-overlay-backdrop').should('not.exist');
    cy.get('input[formcontrolname="appointmentDate"]').should('not.have.value', '');

    // ── Intervalo de la cita (mat-select) ──────────────────────────────
    // Intervalo
    cy.get('mat-select[formcontrolname="slot"]').click();
    cy.get('mat-option').first().click();

    cy.contains('button', 'Agendar').click();
    cy.contains('Confirmar Cita').should('be.visible');
    cy.contains('button', 'Confirmar').click();

    cy.contains('Cita agendada exitosamente').should('be.visible');
    cy.contains('button', 'Aceptar').click();
  });
});
