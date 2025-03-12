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

describe('Presentation e2e test', () => {
  const presentationPageUrl = '/presentation';
  const presentationPageUrlPattern = new RegExp('/presentation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const presentationSample = { plan: 'dans', uri: 'touriste' };

  let presentation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/presentations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/presentations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/presentations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (presentation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/presentations/${presentation.id}`,
      }).then(() => {
        presentation = undefined;
      });
    }
  });

  it('Presentations menu should load Presentations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('presentation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Presentation').should('exist');
    cy.url().should('match', presentationPageUrlPattern);
  });

  describe('Presentation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(presentationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Presentation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/presentation/new$'));
        cy.getEntityCreateUpdateHeading('Presentation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', presentationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/presentations',
          body: presentationSample,
        }).then(({ body }) => {
          presentation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/presentations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/presentations?page=0&size=20>; rel="last",<http://localhost/api/presentations?page=0&size=20>; rel="first"',
              },
              body: [presentation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(presentationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Presentation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('presentation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', presentationPageUrlPattern);
      });

      it('edit button click should load edit Presentation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Presentation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', presentationPageUrlPattern);
      });

      it('edit button click should load edit Presentation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Presentation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', presentationPageUrlPattern);
      });

      it('last delete button click should delete instance of Presentation', () => {
        cy.intercept('GET', '/api/presentations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('presentation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', presentationPageUrlPattern);

        presentation = undefined;
      });
    });
  });

  describe('new Presentation page', () => {
    beforeEach(() => {
      cy.visit(`${presentationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Presentation');
    });

    it('should create an instance of Presentation', () => {
      cy.get(`[data-cy="plan"]`).type('sans que ferme égoïste');
      cy.get(`[data-cy="plan"]`).should('have.value', 'sans que ferme égoïste');

      cy.get(`[data-cy="uri"]`).type('gens bientôt');
      cy.get(`[data-cy="uri"]`).should('have.value', 'gens bientôt');

      cy.get(`[data-cy="promptUserMessage"]`).type('miaou souligner');
      cy.get(`[data-cy="promptUserMessage"]`).should('have.value', 'miaou souligner');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        presentation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', presentationPageUrlPattern);
    });
  });
});
