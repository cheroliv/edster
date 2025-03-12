package edtech.web.rest;

import static edtech.domain.WorkspaceAsserts.*;
import static edtech.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import edtech.IntegrationTest;
import edtech.domain.Workspace;
import edtech.repository.EntityManager;
import edtech.repository.UserRepository;
import edtech.repository.WorkspaceRepository;
import edtech.service.WorkspaceService;
import edtech.service.dto.WorkspaceDTO;
import edtech.service.mapper.WorkspaceMapper;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link WorkspaceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class WorkspaceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PROMPT_SYSTEM_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_PROMPT_SYSTEM_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/workspaces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private WorkspaceRepository workspaceRepositoryMock;

    @Autowired
    private WorkspaceMapper workspaceMapper;

    @Mock
    private WorkspaceService workspaceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Workspace workspace;

    private Workspace insertedWorkspace;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Workspace createEntity() {
        return new Workspace().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).promptSystemMessage(DEFAULT_PROMPT_SYSTEM_MESSAGE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Workspace createUpdatedEntity() {
        return new Workspace().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).promptSystemMessage(UPDATED_PROMPT_SYSTEM_MESSAGE);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Workspace.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        workspace = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedWorkspace != null) {
            workspaceRepository.delete(insertedWorkspace).block();
            insertedWorkspace = null;
        }
        deleteEntities(em);
        userRepository.deleteAllUserAuthorities().block();
        userRepository.deleteAll().block();
    }

    @Test
    void createWorkspace() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Workspace
        WorkspaceDTO workspaceDTO = workspaceMapper.toDto(workspace);
        var returnedWorkspaceDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(workspaceDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(WorkspaceDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Workspace in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWorkspace = workspaceMapper.toEntity(returnedWorkspaceDTO);
        assertWorkspaceUpdatableFieldsEquals(returnedWorkspace, getPersistedWorkspace(returnedWorkspace));

        insertedWorkspace = returnedWorkspace;
    }

    @Test
    void createWorkspaceWithExistingId() throws Exception {
        // Create the Workspace with an existing ID
        workspace.setId(1L);
        WorkspaceDTO workspaceDTO = workspaceMapper.toDto(workspace);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(workspaceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Workspace in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workspace.setName(null);

        // Create the Workspace, which fails.
        WorkspaceDTO workspaceDTO = workspaceMapper.toDto(workspace);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(workspaceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllWorkspaces() {
        // Initialize the database
        insertedWorkspace = workspaceRepository.save(workspace).block();

        // Get all the workspaceList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(workspace.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].promptSystemMessage")
            .value(hasItem(DEFAULT_PROMPT_SYSTEM_MESSAGE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkspacesWithEagerRelationshipsIsEnabled() {
        when(workspaceServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(workspaceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkspacesWithEagerRelationshipsIsNotEnabled() {
        when(workspaceServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(workspaceRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getWorkspace() {
        // Initialize the database
        insertedWorkspace = workspaceRepository.save(workspace).block();

        // Get the workspace
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, workspace.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(workspace.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.promptSystemMessage")
            .value(is(DEFAULT_PROMPT_SYSTEM_MESSAGE));
    }

    @Test
    void getNonExistingWorkspace() {
        // Get the workspace
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingWorkspace() throws Exception {
        // Initialize the database
        insertedWorkspace = workspaceRepository.save(workspace).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workspace
        Workspace updatedWorkspace = workspaceRepository.findById(workspace.getId()).block();
        updatedWorkspace.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).promptSystemMessage(UPDATED_PROMPT_SYSTEM_MESSAGE);
        WorkspaceDTO workspaceDTO = workspaceMapper.toDto(updatedWorkspace);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, workspaceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(workspaceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Workspace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWorkspaceToMatchAllProperties(updatedWorkspace);
    }

    @Test
    void putNonExistingWorkspace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workspace.setId(longCount.incrementAndGet());

        // Create the Workspace
        WorkspaceDTO workspaceDTO = workspaceMapper.toDto(workspace);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, workspaceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(workspaceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Workspace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchWorkspace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workspace.setId(longCount.incrementAndGet());

        // Create the Workspace
        WorkspaceDTO workspaceDTO = workspaceMapper.toDto(workspace);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(workspaceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Workspace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamWorkspace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workspace.setId(longCount.incrementAndGet());

        // Create the Workspace
        WorkspaceDTO workspaceDTO = workspaceMapper.toDto(workspace);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(workspaceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Workspace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateWorkspaceWithPatch() throws Exception {
        // Initialize the database
        insertedWorkspace = workspaceRepository.save(workspace).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workspace using partial update
        Workspace partialUpdatedWorkspace = new Workspace();
        partialUpdatedWorkspace.setId(workspace.getId());

        partialUpdatedWorkspace.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWorkspace.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedWorkspace))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Workspace in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkspaceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWorkspace, workspace),
            getPersistedWorkspace(workspace)
        );
    }

    @Test
    void fullUpdateWorkspaceWithPatch() throws Exception {
        // Initialize the database
        insertedWorkspace = workspaceRepository.save(workspace).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workspace using partial update
        Workspace partialUpdatedWorkspace = new Workspace();
        partialUpdatedWorkspace.setId(workspace.getId());

        partialUpdatedWorkspace.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).promptSystemMessage(UPDATED_PROMPT_SYSTEM_MESSAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWorkspace.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedWorkspace))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Workspace in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkspaceUpdatableFieldsEquals(partialUpdatedWorkspace, getPersistedWorkspace(partialUpdatedWorkspace));
    }

    @Test
    void patchNonExistingWorkspace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workspace.setId(longCount.incrementAndGet());

        // Create the Workspace
        WorkspaceDTO workspaceDTO = workspaceMapper.toDto(workspace);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, workspaceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(workspaceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Workspace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchWorkspace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workspace.setId(longCount.incrementAndGet());

        // Create the Workspace
        WorkspaceDTO workspaceDTO = workspaceMapper.toDto(workspace);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(workspaceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Workspace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamWorkspace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workspace.setId(longCount.incrementAndGet());

        // Create the Workspace
        WorkspaceDTO workspaceDTO = workspaceMapper.toDto(workspace);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(workspaceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Workspace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteWorkspace() {
        // Initialize the database
        insertedWorkspace = workspaceRepository.save(workspace).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the workspace
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, workspace.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return workspaceRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Workspace getPersistedWorkspace(Workspace workspace) {
        return workspaceRepository.findById(workspace.getId()).block();
    }

    protected void assertPersistedWorkspaceToMatchAllProperties(Workspace expectedWorkspace) {
        // Test fails because reactive api returns an empty object instead of null
        // assertWorkspaceAllPropertiesEquals(expectedWorkspace, getPersistedWorkspace(expectedWorkspace));
        assertWorkspaceUpdatableFieldsEquals(expectedWorkspace, getPersistedWorkspace(expectedWorkspace));
    }

    protected void assertPersistedWorkspaceToMatchUpdatableProperties(Workspace expectedWorkspace) {
        // Test fails because reactive api returns an empty object instead of null
        // assertWorkspaceAllUpdatablePropertiesEquals(expectedWorkspace, getPersistedWorkspace(expectedWorkspace));
        assertWorkspaceUpdatableFieldsEquals(expectedWorkspace, getPersistedWorkspace(expectedWorkspace));
    }
}
