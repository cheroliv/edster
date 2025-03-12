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

describe('Workspace e2e test', () => {
  const workspacePageUrl = '/workspace';
  const workspacePageUrlPattern = new RegExp('/workspace(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const workspaceSample = { name: 'sale où' };

  let workspace;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/workspaces+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/workspaces').as('postEntityRequest');
    cy.intercept('DELETE', '/api/workspaces/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (workspace) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/workspaces/${workspace.id}`,
      }).then(() => {
        workspace = undefined;
      });
    }
  });

  it('Workspaces menu should load Workspaces page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('workspace');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Workspace').should('exist');
    cy.url().should('match', workspacePageUrlPattern);
  });

  describe('Workspace page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(workspacePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Workspace page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/workspace/new$'));
        cy.getEntityCreateUpdateHeading('Workspace');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', workspacePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/workspaces',
          body: workspaceSample,
        }).then(({ body }) => {
          workspace = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/workspaces+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/workspaces?page=0&size=20>; rel="last",<http://localhost/api/workspaces?page=0&size=20>; rel="first"',
              },
              body: [workspace],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(workspacePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Workspace page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('workspace');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', workspacePageUrlPattern);
      });

      it('edit button click should load edit Workspace page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Workspace');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', workspacePageUrlPattern);
      });

      it('edit button click should load edit Workspace page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Workspace');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', workspacePageUrlPattern);
      });

      it('last delete button click should delete instance of Workspace', () => {
        cy.intercept('GET', '/api/workspaces/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('workspace').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', workspacePageUrlPattern);

        workspace = undefined;
      });
    });
  });

  describe('new Workspace page', () => {
    beforeEach(() => {
      cy.visit(`${workspacePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Workspace');
    });

    it('should create an instance of Workspace', () => {
      cy.get(`[data-cy="name"]`).type('membre titulaire');
      cy.get(`[data-cy="name"]`).should('have.value', 'membre titulaire');

      cy.get(`[data-cy="description"]`).type('groin groin bien que');
      cy.get(`[data-cy="description"]`).should('have.value', 'groin groin bien que');

      cy.get(`[data-cy="promptSystemMessage"]`).type('à demi antagoniste de façon à ce que');
      cy.get(`[data-cy="promptSystemMessage"]`).should('have.value', 'à demi antagoniste de façon à ce que');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        workspace = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', workspacePageUrlPattern);
    });
  });
});
