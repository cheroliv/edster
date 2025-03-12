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

describe('PlantUMLDiagramResource e2e test', () => {
  const plantUMLDiagramResourcePageUrl = '/plant-uml-diagram-resource';
  const plantUMLDiagramResourcePageUrlPattern = new RegExp('/plant-uml-diagram-resource(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const plantUMLDiagramResourceSample = { type: 'PLANTUML' };

  let plantUMLDiagramResource;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/plant-uml-diagram-resources+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/plant-uml-diagram-resources').as('postEntityRequest');
    cy.intercept('DELETE', '/api/plant-uml-diagram-resources/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (plantUMLDiagramResource) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/plant-uml-diagram-resources/${plantUMLDiagramResource.id}`,
      }).then(() => {
        plantUMLDiagramResource = undefined;
      });
    }
  });

  it('PlantUMLDiagramResources menu should load PlantUMLDiagramResources page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('plant-uml-diagram-resource');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PlantUMLDiagramResource').should('exist');
    cy.url().should('match', plantUMLDiagramResourcePageUrlPattern);
  });

  describe('PlantUMLDiagramResource page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(plantUMLDiagramResourcePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PlantUMLDiagramResource page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/plant-uml-diagram-resource/new$'));
        cy.getEntityCreateUpdateHeading('PlantUMLDiagramResource');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', plantUMLDiagramResourcePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/plant-uml-diagram-resources',
          body: plantUMLDiagramResourceSample,
        }).then(({ body }) => {
          plantUMLDiagramResource = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/plant-uml-diagram-resources+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/plant-uml-diagram-resources?page=0&size=20>; rel="last",<http://localhost/api/plant-uml-diagram-resources?page=0&size=20>; rel="first"',
              },
              body: [plantUMLDiagramResource],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(plantUMLDiagramResourcePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PlantUMLDiagramResource page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('plantUMLDiagramResource');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', plantUMLDiagramResourcePageUrlPattern);
      });

      it('edit button click should load edit PlantUMLDiagramResource page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PlantUMLDiagramResource');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', plantUMLDiagramResourcePageUrlPattern);
      });

      it('edit button click should load edit PlantUMLDiagramResource page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PlantUMLDiagramResource');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', plantUMLDiagramResourcePageUrlPattern);
      });

      it('last delete button click should delete instance of PlantUMLDiagramResource', () => {
        cy.intercept('GET', '/api/plant-uml-diagram-resources/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('plantUMLDiagramResource').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', plantUMLDiagramResourcePageUrlPattern);

        plantUMLDiagramResource = undefined;
      });
    });
  });

  describe('new PlantUMLDiagramResource page', () => {
    beforeEach(() => {
      cy.visit(`${plantUMLDiagramResourcePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PlantUMLDiagramResource');
    });

    it('should create an instance of PlantUMLDiagramResource', () => {
      cy.get(`[data-cy="type"]`).select('IMAGE');

      cy.get(`[data-cy="uri"]`).type('croâ');
      cy.get(`[data-cy="uri"]`).should('have.value', 'croâ');

      cy.get(`[data-cy="umlCode"]`).type('que psitt');
      cy.get(`[data-cy="umlCode"]`).should('have.value', 'que psitt');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        plantUMLDiagramResource = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', plantUMLDiagramResourcePageUrlPattern);
    });
  });
});
