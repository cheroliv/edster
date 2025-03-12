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

describe('ImageResource e2e test', () => {
  const imageResourcePageUrl = '/image-resource';
  const imageResourcePageUrlPattern = new RegExp('/image-resource(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const imageResourceSample = { type: 'IMAGE' };

  let imageResource;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/image-resources+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/image-resources').as('postEntityRequest');
    cy.intercept('DELETE', '/api/image-resources/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (imageResource) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/image-resources/${imageResource.id}`,
      }).then(() => {
        imageResource = undefined;
      });
    }
  });

  it('ImageResources menu should load ImageResources page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('image-resource');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ImageResource').should('exist');
    cy.url().should('match', imageResourcePageUrlPattern);
  });

  describe('ImageResource page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(imageResourcePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ImageResource page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/image-resource/new$'));
        cy.getEntityCreateUpdateHeading('ImageResource');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', imageResourcePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/image-resources',
          body: imageResourceSample,
        }).then(({ body }) => {
          imageResource = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/image-resources+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/image-resources?page=0&size=20>; rel="last",<http://localhost/api/image-resources?page=0&size=20>; rel="first"',
              },
              body: [imageResource],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(imageResourcePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ImageResource page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('imageResource');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', imageResourcePageUrlPattern);
      });

      it('edit button click should load edit ImageResource page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ImageResource');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', imageResourcePageUrlPattern);
      });

      it('edit button click should load edit ImageResource page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ImageResource');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', imageResourcePageUrlPattern);
      });

      it('last delete button click should delete instance of ImageResource', () => {
        cy.intercept('GET', '/api/image-resources/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('imageResource').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', imageResourcePageUrlPattern);

        imageResource = undefined;
      });
    });
  });

  describe('new ImageResource page', () => {
    beforeEach(() => {
      cy.visit(`${imageResourcePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ImageResource');
    });

    it('should create an instance of ImageResource', () => {
      cy.get(`[data-cy="type"]`).select('IMAGE');

      cy.get(`[data-cy="uri"]`).type('pleurer alors');
      cy.get(`[data-cy="uri"]`).should('have.value', 'pleurer alors');

      cy.get(`[data-cy="resolution"]`).type('repasser ensemble émérite');
      cy.get(`[data-cy="resolution"]`).should('have.value', 'repasser ensemble émérite');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        imageResource = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', imageResourcePageUrlPattern);
    });
  });
});
