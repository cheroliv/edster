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
  // const presentationSample = {"name":"biathlète assez secours"};

  let presentation;
  // let user;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"login":"9","firstName":"Ascelin","lastName":"Lefebvre","email":"Renaud.Morin18@hotmail.fr","imageUrl":"autrement","langKey":"au dépens "},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/presentations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/presentations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/presentations/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

  });
   */

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

  /* Disabled due to incompatibility
  afterEach(() => {
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
  });
   */

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
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/presentations',
          body: {
            ...presentationSample,
            user: user,
          },
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
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(presentationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(presentationPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
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

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of Presentation', () => {
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

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of Presentation', () => {
      cy.get(`[data-cy="name"]`).type('vroum pff sauf à');
      cy.get(`[data-cy="name"]`).should('have.value', 'vroum pff sauf à');

      cy.get(`[data-cy="plan"]`).type('voyager puisque souple');
      cy.get(`[data-cy="plan"]`).should('have.value', 'voyager puisque souple');

      cy.get(`[data-cy="uri"]`).type('personnel membre du personnel');
      cy.get(`[data-cy="uri"]`).should('have.value', 'personnel membre du personnel');

      cy.get(`[data-cy="user"]`).select(1);

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
