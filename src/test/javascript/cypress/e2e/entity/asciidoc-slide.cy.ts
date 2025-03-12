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

describe('AsciidocSlide e2e test', () => {
  const asciidocSlidePageUrl = '/asciidoc-slide';
  const asciidocSlidePageUrlPattern = new RegExp('/asciidoc-slide(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const asciidocSlideSample = { title: 'déduire réchauffer' };

  let asciidocSlide;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/asciidoc-slides+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/asciidoc-slides').as('postEntityRequest');
    cy.intercept('DELETE', '/api/asciidoc-slides/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (asciidocSlide) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asciidoc-slides/${asciidocSlide.id}`,
      }).then(() => {
        asciidocSlide = undefined;
      });
    }
  });

  it('AsciidocSlides menu should load AsciidocSlides page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asciidoc-slide');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AsciidocSlide').should('exist');
    cy.url().should('match', asciidocSlidePageUrlPattern);
  });

  describe('AsciidocSlide page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(asciidocSlidePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AsciidocSlide page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/asciidoc-slide/new$'));
        cy.getEntityCreateUpdateHeading('AsciidocSlide');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', asciidocSlidePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/asciidoc-slides',
          body: asciidocSlideSample,
        }).then(({ body }) => {
          asciidocSlide = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/asciidoc-slides+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/asciidoc-slides?page=0&size=20>; rel="last",<http://localhost/api/asciidoc-slides?page=0&size=20>; rel="first"',
              },
              body: [asciidocSlide],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(asciidocSlidePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AsciidocSlide page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('asciidocSlide');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', asciidocSlidePageUrlPattern);
      });

      it('edit button click should load edit AsciidocSlide page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AsciidocSlide');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', asciidocSlidePageUrlPattern);
      });

      it('edit button click should load edit AsciidocSlide page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AsciidocSlide');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', asciidocSlidePageUrlPattern);
      });

      it('last delete button click should delete instance of AsciidocSlide', () => {
        cy.intercept('GET', '/api/asciidoc-slides/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('asciidocSlide').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', asciidocSlidePageUrlPattern);

        asciidocSlide = undefined;
      });
    });
  });

  describe('new AsciidocSlide page', () => {
    beforeEach(() => {
      cy.visit(`${asciidocSlidePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AsciidocSlide');
    });

    it('should create an instance of AsciidocSlide', () => {
      cy.get(`[data-cy="title"]`).type('désagréable miam');
      cy.get(`[data-cy="title"]`).should('have.value', 'désagréable miam');

      cy.get(`[data-cy="content"]`).type('sembler');
      cy.get(`[data-cy="content"]`).should('have.value', 'sembler');

      cy.get(`[data-cy="notes"]`).type('sincère');
      cy.get(`[data-cy="notes"]`).should('have.value', 'sincère');

      cy.get(`[data-cy="num"]`).type('6628');
      cy.get(`[data-cy="num"]`).should('have.value', '6628');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        asciidocSlide = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', asciidocSlidePageUrlPattern);
    });
  });
});
