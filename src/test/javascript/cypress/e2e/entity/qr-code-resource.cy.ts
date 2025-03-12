import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('QRCodeResource e2e test', () => {
  const qRCodeResourcePageUrl = '/qr-code-resource';
  const qRCodeResourcePageUrlPattern = new RegExp('/qr-code-resource(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const qRCodeResourceSample = { type: 'IMAGE' };

  let qRCodeResource;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/qr-code-resources+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/qr-code-resources').as('postEntityRequest');
    cy.intercept('DELETE', '/api/qr-code-resources/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (qRCodeResource) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/qr-code-resources/${qRCodeResource.id}`,
      }).then(() => {
        qRCodeResource = undefined;
      });
    }
  });

  it('QRCodeResources menu should load QRCodeResources page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('qr-code-resource');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('QRCodeResource').should('exist');
    cy.url().should('match', qRCodeResourcePageUrlPattern);
  });

  describe('QRCodeResource page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(qRCodeResourcePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create QRCodeResource page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/qr-code-resource/new$'));
        cy.getEntityCreateUpdateHeading('QRCodeResource');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', qRCodeResourcePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/qr-code-resources',
          body: qRCodeResourceSample,
        }).then(({ body }) => {
          qRCodeResource = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/qr-code-resources+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/qr-code-resources?page=0&size=20>; rel="last",<http://localhost/api/qr-code-resources?page=0&size=20>; rel="first"',
              },
              body: [qRCodeResource],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(qRCodeResourcePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details QRCodeResource page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('qRCodeResource');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', qRCodeResourcePageUrlPattern);
      });

      it('edit button click should load edit QRCodeResource page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QRCodeResource');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', qRCodeResourcePageUrlPattern);
      });

      it('edit button click should load edit QRCodeResource page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QRCodeResource');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', qRCodeResourcePageUrlPattern);
      });

      it('last delete button click should delete instance of QRCodeResource', () => {
        cy.intercept('GET', '/api/qr-code-resources/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('qRCodeResource').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', qRCodeResourcePageUrlPattern);

        qRCodeResource = undefined;
      });
    });
  });

  describe('new QRCodeResource page', () => {
    beforeEach(() => {
      cy.visit(`${qRCodeResourcePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('QRCodeResource');
    });

    it('should create an instance of QRCodeResource', () => {
      cy.get(`[data-cy="type"]`).select('QRCODE');

      cy.get(`[data-cy="uri"]`).type('adepte');
      cy.get(`[data-cy="uri"]`).should('have.value', 'adepte');

      cy.get(`[data-cy="data"]`).type('à même');
      cy.get(`[data-cy="data"]`).should('have.value', 'à même');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        qRCodeResource = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', qRCodeResourcePageUrlPattern);
    });
  });
});
