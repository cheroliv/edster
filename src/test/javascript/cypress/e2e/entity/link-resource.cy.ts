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

describe('LinkResource e2e test', () => {
  const linkResourcePageUrl = '/link-resource';
  const linkResourcePageUrlPattern = new RegExp('/link-resource(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const linkResourceSample = { type: 'QRCODE' };

  let linkResource;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/link-resources+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/link-resources').as('postEntityRequest');
    cy.intercept('DELETE', '/api/link-resources/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (linkResource) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/link-resources/${linkResource.id}`,
      }).then(() => {
        linkResource = undefined;
      });
    }
  });

  it('LinkResources menu should load LinkResources page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('link-resource');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LinkResource').should('exist');
    cy.url().should('match', linkResourcePageUrlPattern);
  });

  describe('LinkResource page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(linkResourcePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LinkResource page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/link-resource/new$'));
        cy.getEntityCreateUpdateHeading('LinkResource');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', linkResourcePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/link-resources',
          body: linkResourceSample,
        }).then(({ body }) => {
          linkResource = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/link-resources+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/link-resources?page=0&size=20>; rel="last",<http://localhost/api/link-resources?page=0&size=20>; rel="first"',
              },
              body: [linkResource],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(linkResourcePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LinkResource page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('linkResource');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', linkResourcePageUrlPattern);
      });

      it('edit button click should load edit LinkResource page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LinkResource');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', linkResourcePageUrlPattern);
      });

      it('edit button click should load edit LinkResource page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LinkResource');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', linkResourcePageUrlPattern);
      });

      it('last delete button click should delete instance of LinkResource', () => {
        cy.intercept('GET', '/api/link-resources/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('linkResource').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', linkResourcePageUrlPattern);

        linkResource = undefined;
      });
    });
  });

  describe('new LinkResource page', () => {
    beforeEach(() => {
      cy.visit(`${linkResourcePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('LinkResource');
    });

    it('should create an instance of LinkResource', () => {
      cy.get(`[data-cy="type"]`).select('QRCODE');

      cy.get(`[data-cy="uri"]`).type('miaou géométrique');
      cy.get(`[data-cy="uri"]`).should('have.value', 'miaou géométrique');

      cy.get(`[data-cy="target"]`).type('de façon que dense à force de');
      cy.get(`[data-cy="target"]`).should('have.value', 'de façon que dense à force de');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        linkResource = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', linkResourcePageUrlPattern);
    });
  });
});
