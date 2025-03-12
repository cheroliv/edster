package edtech.web.rest;

import static edtech.domain.PresentationAsserts.*;
import static edtech.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import edtech.IntegrationTest;
import edtech.domain.Presentation;
import edtech.repository.EntityManager;
import edtech.repository.PresentationRepository;
import edtech.service.dto.PresentationDTO;
import edtech.service.mapper.PresentationMapper;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link PresentationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PresentationResourceIT {

    private static final String DEFAULT_PLAN = "AAAAAAAAAA";
    private static final String UPDATED_PLAN = "BBBBBBBBBB";

    private static final String DEFAULT_URI = "AAAAAAAAAA";
    private static final String UPDATED_URI = "BBBBBBBBBB";

    private static final String DEFAULT_PROMPT_USER_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_PROMPT_USER_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/presentations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PresentationRepository presentationRepository;

    @Autowired
    private PresentationMapper presentationMapper;

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
    public static Presentation createEntity() {
        return new Presentation().plan(DEFAULT_PLAN).uri(DEFAULT_URI).promptUserMessage(DEFAULT_PROMPT_USER_MESSAGE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presentation createUpdatedEntity() {
        return new Presentation().plan(UPDATED_PLAN).uri(UPDATED_URI).promptUserMessage(UPDATED_PROMPT_USER_MESSAGE);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Presentation.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        presentation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPresentation != null) {
            presentationRepository.delete(insertedPresentation).block();
            insertedPresentation = null;
        }
        deleteEntities(em);
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
    void checkPlanIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        presentation.setPlan(null);

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
    void checkUriIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        presentation.setUri(null);

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
            .jsonPath("$.[*].plan")
            .value(hasItem(DEFAULT_PLAN))
            .jsonPath("$.[*].uri")
            .value(hasItem(DEFAULT_URI))
            .jsonPath("$.[*].promptUserMessage")
            .value(hasItem(DEFAULT_PROMPT_USER_MESSAGE));
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
            .jsonPath("$.plan")
            .value(is(DEFAULT_PLAN))
            .jsonPath("$.uri")
            .value(is(DEFAULT_URI))
            .jsonPath("$.promptUserMessage")
            .value(is(DEFAULT_PROMPT_USER_MESSAGE));
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
        updatedPresentation.plan(UPDATED_PLAN).uri(UPDATED_URI).promptUserMessage(UPDATED_PROMPT_USER_MESSAGE);
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

        partialUpdatedPresentation.plan(UPDATED_PLAN);

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

        partialUpdatedPresentation.plan(UPDATED_PLAN).uri(UPDATED_URI).promptUserMessage(UPDATED_PROMPT_USER_MESSAGE);

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
