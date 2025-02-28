package education.cccp.edtech.web.rest;

import static education.cccp.edtech.domain.PresentationAsserts.*;
import static education.cccp.edtech.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import education.cccp.edtech.IntegrationTest;
import education.cccp.edtech.domain.Presentation;
import education.cccp.edtech.domain.User;
import education.cccp.edtech.repository.EntityManager;
import education.cccp.edtech.repository.PresentationRepository;
import education.cccp.edtech.repository.UserRepository;
import education.cccp.edtech.repository.UserRepository;
import education.cccp.edtech.service.PresentationService;
import education.cccp.edtech.service.dto.PresentationDTO;
import education.cccp.edtech.service.mapper.PresentationMapper;
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
 * Integration tests for the {@link PresentationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PresentationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PLAN = "AAAAAAAAAA";
    private static final String UPDATED_PLAN = "BBBBBBBBBB";

    private static final String DEFAULT_URI = "AAAAAAAAAA";
    private static final String UPDATED_URI = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/presentations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PresentationRepository presentationRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private PresentationRepository presentationRepositoryMock;

    @Autowired
    private PresentationMapper presentationMapper;

    @Mock
    private PresentationService presentationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Presentation presentation;

    private Presentation insertedPresentation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presentation createEntity(EntityManager em) {
        Presentation presentation = new Presentation().name(DEFAULT_NAME).plan(DEFAULT_PLAN).uri(DEFAULT_URI);
        // Add required entity
        User user = em.insert(UserResourceIT.createEntity()).block();
        presentation.setUser(user);
        return presentation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presentation createUpdatedEntity(EntityManager em) {
        Presentation updatedPresentation = new Presentation().name(UPDATED_NAME).plan(UPDATED_PLAN).uri(UPDATED_URI);
        // Add required entity
        User user = em.insert(UserResourceIT.createEntity()).block();
        updatedPresentation.setUser(user);
        return updatedPresentation;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Presentation.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        UserResourceIT.deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        presentation = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPresentation != null) {
            presentationRepository.delete(insertedPresentation).block();
            insertedPresentation = null;
        }
        deleteEntities(em);
        userRepository.deleteAllUserAuthorities().block();
        userRepository.deleteAll().block();
    }

    @Test
    void createPresentation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Presentation
        PresentationDTO presentationDTO = presentationMapper.toDto(presentation);
        var returnedPresentationDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(presentationDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(PresentationDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Presentation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPresentation = presentationMapper.toEntity(returnedPresentationDTO);
        assertPresentationUpdatableFieldsEquals(returnedPresentation, getPersistedPresentation(returnedPresentation));

        insertedPresentation = returnedPresentation;
    }

    @Test
    void createPresentationWithExistingId() throws Exception {
        // Create the Presentation with an existing ID
        presentation.setId(1L);
        PresentationDTO presentationDTO = presentationMapper.toDto(presentation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(presentationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Presentation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        presentation.setName(null);

        // Create the Presentation, which fails.
        PresentationDTO presentationDTO = presentationMapper.toDto(presentation);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(presentationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllPresentations() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        // Get all the presentationList
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
            .value(hasItem(presentation.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].plan")
            .value(hasItem(DEFAULT_PLAN))
            .jsonPath("$.[*].uri")
            .value(hasItem(DEFAULT_URI));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPresentationsWithEagerRelationshipsIsEnabled() {
        when(presentationServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(presentationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPresentationsWithEagerRelationshipsIsNotEnabled() {
        when(presentationServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(presentationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPresentation() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        // Get the presentation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, presentation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(presentation.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.plan")
            .value(is(DEFAULT_PLAN))
            .jsonPath("$.uri")
            .value(is(DEFAULT_URI));
    }

    @Test
    void getPresentationsByIdFiltering() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        Long id = presentation.getId();

        defaultPresentationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPresentationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPresentationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    void getAllPresentationsByNameIsEqualToSomething() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        // Get all the presentationList where name equals to
        defaultPresentationFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    void getAllPresentationsByNameIsInShouldWork() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        // Get all the presentationList where name in
        defaultPresentationFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    void getAllPresentationsByNameIsNullOrNotNull() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        // Get all the presentationList where name is not null
        defaultPresentationFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    void getAllPresentationsByNameContainsSomething() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        // Get all the presentationList where name contains
        defaultPresentationFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    void getAllPresentationsByNameNotContainsSomething() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        // Get all the presentationList where name does not contain
        defaultPresentationFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    void getAllPresentationsByPlanIsEqualToSomething() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        // Get all the presentationList where plan equals to
        defaultPresentationFiltering("plan.equals=" + DEFAULT_PLAN, "plan.equals=" + UPDATED_PLAN);
    }

    @Test
    void getAllPresentationsByPlanIsInShouldWork() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        // Get all the presentationList where plan in
        defaultPresentationFiltering("plan.in=" + DEFAULT_PLAN + "," + UPDATED_PLAN, "plan.in=" + UPDATED_PLAN);
    }

    @Test
    void getAllPresentationsByPlanIsNullOrNotNull() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        // Get all the presentationList where plan is not null
        defaultPresentationFiltering("plan.specified=true", "plan.specified=false");
    }

    @Test
    void getAllPresentationsByPlanContainsSomething() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        // Get all the presentationList where plan contains
        defaultPresentationFiltering("plan.contains=" + DEFAULT_PLAN, "plan.contains=" + UPDATED_PLAN);
    }

    @Test
    void getAllPresentationsByPlanNotContainsSomething() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        // Get all the presentationList where plan does not contain
        defaultPresentationFiltering("plan.doesNotContain=" + UPDATED_PLAN, "plan.doesNotContain=" + DEFAULT_PLAN);
    }

    @Test
    void getAllPresentationsByUriIsEqualToSomething() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        // Get all the presentationList where uri equals to
        defaultPresentationFiltering("uri.equals=" + DEFAULT_URI, "uri.equals=" + UPDATED_URI);
    }

    @Test
    void getAllPresentationsByUriIsInShouldWork() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        // Get all the presentationList where uri in
        defaultPresentationFiltering("uri.in=" + DEFAULT_URI + "," + UPDATED_URI, "uri.in=" + UPDATED_URI);
    }

    @Test
    void getAllPresentationsByUriIsNullOrNotNull() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        // Get all the presentationList where uri is not null
        defaultPresentationFiltering("uri.specified=true", "uri.specified=false");
    }

    @Test
    void getAllPresentationsByUriContainsSomething() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        // Get all the presentationList where uri contains
        defaultPresentationFiltering("uri.contains=" + DEFAULT_URI, "uri.contains=" + UPDATED_URI);
    }

    @Test
    void getAllPresentationsByUriNotContainsSomething() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        // Get all the presentationList where uri does not contain
        defaultPresentationFiltering("uri.doesNotContain=" + UPDATED_URI, "uri.doesNotContain=" + DEFAULT_URI);
    }

    @Test
    void getAllPresentationsByUserIsEqualToSomething() {
        User user = UserResourceIT.createEntity();
        userRepository.save(user).block();
        Long userId = user.getId();
        presentation.setUserId(userId);
        insertedPresentation = presentationRepository.save(presentation).block();
        // Get all the presentationList where user equals to userId
        defaultPresentationShouldBeFound("userId.equals=" + userId);

        // Get all the presentationList where user equals to (userId + 1)
        defaultPresentationShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultPresentationFiltering(String shouldBeFound, String shouldNotBeFound) {
        defaultPresentationShouldBeFound(shouldBeFound);
        defaultPresentationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPresentationShouldBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(presentation.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].plan")
            .value(hasItem(DEFAULT_PLAN))
            .jsonPath("$.[*].uri")
            .value(hasItem(DEFAULT_URI));

        // Check, that the count call also returns 1
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .value(is(1));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPresentationShouldNotBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .isArray()
            .jsonPath("$")
            .isEmpty();

        // Check, that the count call also returns 0
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .value(is(0));
    }

    @Test
    void getNonExistingPresentation() {
        // Get the presentation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPresentation() throws Exception {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the presentation
        Presentation updatedPresentation = presentationRepository.findById(presentation.getId()).block();
        updatedPresentation.name(UPDATED_NAME).plan(UPDATED_PLAN).uri(UPDATED_URI);
        PresentationDTO presentationDTO = presentationMapper.toDto(updatedPresentation);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, presentationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(presentationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Presentation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPresentationToMatchAllProperties(updatedPresentation);
    }

    @Test
    void putNonExistingPresentation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        presentation.setId(longCount.incrementAndGet());

        // Create the Presentation
        PresentationDTO presentationDTO = presentationMapper.toDto(presentation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, presentationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(presentationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Presentation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPresentation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        presentation.setId(longCount.incrementAndGet());

        // Create the Presentation
        PresentationDTO presentationDTO = presentationMapper.toDto(presentation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(presentationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Presentation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPresentation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        presentation.setId(longCount.incrementAndGet());

        // Create the Presentation
        PresentationDTO presentationDTO = presentationMapper.toDto(presentation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(presentationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Presentation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePresentationWithPatch() throws Exception {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the presentation using partial update
        Presentation partialUpdatedPresentation = new Presentation();
        partialUpdatedPresentation.setId(presentation.getId());

        partialUpdatedPresentation.uri(UPDATED_URI);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPresentation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPresentation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Presentation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPresentationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPresentation, presentation),
            getPersistedPresentation(presentation)
        );
    }

    @Test
    void fullUpdatePresentationWithPatch() throws Exception {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the presentation using partial update
        Presentation partialUpdatedPresentation = new Presentation();
        partialUpdatedPresentation.setId(presentation.getId());

        partialUpdatedPresentation.name(UPDATED_NAME).plan(UPDATED_PLAN).uri(UPDATED_URI);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPresentation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPresentation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Presentation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPresentationUpdatableFieldsEquals(partialUpdatedPresentation, getPersistedPresentation(partialUpdatedPresentation));
    }

    @Test
    void patchNonExistingPresentation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        presentation.setId(longCount.incrementAndGet());

        // Create the Presentation
        PresentationDTO presentationDTO = presentationMapper.toDto(presentation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, presentationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(presentationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Presentation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPresentation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        presentation.setId(longCount.incrementAndGet());

        // Create the Presentation
        PresentationDTO presentationDTO = presentationMapper.toDto(presentation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(presentationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Presentation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPresentation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        presentation.setId(longCount.incrementAndGet());

        // Create the Presentation
        PresentationDTO presentationDTO = presentationMapper.toDto(presentation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(presentationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Presentation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePresentation() {
        // Initialize the database
        insertedPresentation = presentationRepository.save(presentation).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the presentation
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, presentation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return presentationRepository.count().block();
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

    protected Presentation getPersistedPresentation(Presentation presentation) {
        return presentationRepository.findById(presentation.getId()).block();
    }

    protected void assertPersistedPresentationToMatchAllProperties(Presentation expectedPresentation) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPresentationAllPropertiesEquals(expectedPresentation, getPersistedPresentation(expectedPresentation));
        assertPresentationUpdatableFieldsEquals(expectedPresentation, getPersistedPresentation(expectedPresentation));
    }

    protected void assertPersistedPresentationToMatchUpdatableProperties(Presentation expectedPresentation) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPresentationAllUpdatablePropertiesEquals(expectedPresentation, getPersistedPresentation(expectedPresentation));
        assertPresentationUpdatableFieldsEquals(expectedPresentation, getPersistedPresentation(expectedPresentation));
    }
}
