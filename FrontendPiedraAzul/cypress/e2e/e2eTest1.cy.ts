describe('Front Pacientes — Flujo completo', () => {
  const loginAsPatient = () => {
    cy.visit('/login');
    cy.get('input[formcontrolname="email"]').should('be.visible').clear().type('angela@gmail.com');
    cy.get('input[formcontrolname="password"]').should('be.visible').clear().type('12345678');
    cy.contains('button', 'Ingresar').click();
  };

  it('Paciente puede iniciar sesión y abrir el formulario de agendamiento', () => {
    loginAsPatient();

    cy.visit('/citas/agendar');

    cy.contains('Agendar Cita').should('be.visible');
    cy.contains('Selecciona tu especialidad').should('be.visible');
  });

  it('Paciente puede abrir su cuenta', () => {
    loginAsPatient();

    cy.visit('/mi-cuenta');

    cy.contains('Mi cuenta').should('be.visible');
    cy.contains('Informacion del paciente').should('be.visible');
  });
});