package edtech.web.rest;

import static edtech.domain.AsciidocSlideAsserts.*;
import static edtech.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import edtech.IntegrationTest;
import edtech.domain.AsciidocSlide;
import edtech.repository.AsciidocSlideRepository;
import edtech.repository.EntityManager;
import edtech.service.dto.AsciidocSlideDTO;
import edtech.service.mapper.AsciidocSlideMapper;
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
 * Integration tests for the {@link AsciidocSlideResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AsciidocSlideResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUM = 1;
    private static final Integer UPDATED_NUM = 2;

    private static final String ENTITY_API_URL = "/api/asciidoc-slides";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AsciidocSlideRepository asciidocSlideRepository;

    @Autowired
    private AsciidocSlideMapper asciidocSlideMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AsciidocSlide asciidocSlide;

    private AsciidocSlide insertedAsciidocSlide;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AsciidocSlide createEntity() {
        return new AsciidocSlide().title(DEFAULT_TITLE).content(DEFAULT_CONTENT).notes(DEFAULT_NOTES).num(DEFAULT_NUM);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AsciidocSlide createUpdatedEntity() {
        return new AsciidocSlide().title(UPDATED_TITLE).content(UPDATED_CONTENT).notes(UPDATED_NOTES).num(UPDATED_NUM);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AsciidocSlide.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        asciidocSlide = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAsciidocSlide != null) {
            asciidocSlideRepository.delete(insertedAsciidocSlide).block();
            insertedAsciidocSlide = null;
        }
        deleteEntities(em);
    }

    @Test
    void createAsciidocSlide() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AsciidocSlide
        AsciidocSlideDTO asciidocSlideDTO = asciidocSlideMapper.toDto(asciidocSlide);
        var returnedAsciidocSlideDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(asciidocSlideDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(AsciidocSlideDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the AsciidocSlide in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAsciidocSlide = asciidocSlideMapper.toEntity(returnedAsciidocSlideDTO);
        assertAsciidocSlideUpdatableFieldsEquals(returnedAsciidocSlide, getPersistedAsciidocSlide(returnedAsciidocSlide));

        insertedAsciidocSlide = returnedAsciidocSlide;
    }

    @Test
    void createAsciidocSlideWithExistingId() throws Exception {
        // Create the AsciidocSlide with an existing ID
        asciidocSlide.setId(1L);
        AsciidocSlideDTO asciidocSlideDTO = asciidocSlideMapper.toDto(asciidocSlide);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(asciidocSlideDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AsciidocSlide in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllAsciidocSlides() {
        // Initialize the database
        insertedAsciidocSlide = asciidocSlideRepository.save(asciidocSlide).block();

        // Get all the asciidocSlideList
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
            .value(hasItem(asciidocSlide.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT))
            .jsonPath("$.[*].notes")
            .value(hasItem(DEFAULT_NOTES))
            .jsonPath("$.[*].num")
            .value(hasItem(DEFAULT_NUM));
    }

    @Test
    void getAsciidocSlide() {
        // Initialize the database
        insertedAsciidocSlide = asciidocSlideRepository.save(asciidocSlide).block();

        // Get the asciidocSlide
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, asciidocSlide.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(asciidocSlide.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.content")
            .value(is(DEFAULT_CONTENT))
            .jsonPath("$.notes")
            .value(is(DEFAULT_NOTES))
            .jsonPath("$.num")
            .value(is(DEFAULT_NUM));
    }

    @Test
    void getNonExistingAsciidocSlide() {
        // Get the asciidocSlide
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAsciidocSlide() throws Exception {
        // Initialize the database
        insertedAsciidocSlide = asciidocSlideRepository.save(asciidocSlide).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the asciidocSlide
        AsciidocSlide updatedAsciidocSlide = asciidocSlideRepository.findById(asciidocSlide.getId()).block();
        updatedAsciidocSlide.title(UPDATED_TITLE).content(UPDATED_CONTENT).notes(UPDATED_NOTES).num(UPDATED_NUM);
        AsciidocSlideDTO asciidocSlideDTO = asciidocSlideMapper.toDto(updatedAsciidocSlide);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, asciidocSlideDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(asciidocSlideDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AsciidocSlide in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAsciidocSlideToMatchAllProperties(updatedAsciidocSlide);
    }

    @Test
    void putNonExistingAsciidocSlide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        asciidocSlide.setId(longCount.incrementAndGet());

        // Create the AsciidocSlide
        AsciidocSlideDTO asciidocSlideDTO = asciidocSlideMapper.toDto(asciidocSlide);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, asciidocSlideDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(asciidocSlideDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AsciidocSlide in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAsciidocSlide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        asciidocSlide.setId(longCount.incrementAndGet());

        // Create the AsciidocSlide
        AsciidocSlideDTO asciidocSlideDTO = asciidocSlideMapper.toDto(asciidocSlide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(asciidocSlideDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AsciidocSlide in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAsciidocSlide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        asciidocSlide.setId(longCount.incrementAndGet());

        // Create the AsciidocSlide
        AsciidocSlideDTO asciidocSlideDTO = asciidocSlideMapper.toDto(asciidocSlide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(asciidocSlideDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AsciidocSlide in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAsciidocSlideWithPatch() throws Exception {
        // Initialize the database
        insertedAsciidocSlide = asciidocSlideRepository.save(asciidocSlide).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the asciidocSlide using partial update
        AsciidocSlide partialUpdatedAsciidocSlide = new AsciidocSlide();
        partialUpdatedAsciidocSlide.setId(asciidocSlide.getId());

        partialUpdatedAsciidocSlide.title(UPDATED_TITLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAsciidocSlide.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAsciidocSlide))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AsciidocSlide in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAsciidocSlideUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAsciidocSlide, asciidocSlide),
            getPersistedAsciidocSlide(asciidocSlide)
        );
    }

    @Test
    void fullUpdateAsciidocSlideWithPatch() throws Exception {
        // Initialize the database
        insertedAsciidocSlide = asciidocSlideRepository.save(asciidocSlide).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the asciidocSlide using partial update
        AsciidocSlide partialUpdatedAsciidocSlide = new AsciidocSlide();
        partialUpdatedAsciidocSlide.setId(asciidocSlide.getId());

        partialUpdatedAsciidocSlide.title(UPDATED_TITLE).content(UPDATED_CONTENT).notes(UPDATED_NOTES).num(UPDATED_NUM);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAsciidocSlide.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAsciidocSlide))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AsciidocSlide in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAsciidocSlideUpdatableFieldsEquals(partialUpdatedAsciidocSlide, getPersistedAsciidocSlide(partialUpdatedAsciidocSlide));
    }

    @Test
    void patchNonExistingAsciidocSlide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        asciidocSlide.setId(longCount.incrementAndGet());

        // Create the AsciidocSlide
        AsciidocSlideDTO asciidocSlideDTO = asciidocSlideMapper.toDto(asciidocSlide);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, asciidocSlideDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(asciidocSlideDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AsciidocSlide in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAsciidocSlide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        asciidocSlide.setId(longCount.incrementAndGet());

        // Create the AsciidocSlide
        AsciidocSlideDTO asciidocSlideDTO = asciidocSlideMapper.toDto(asciidocSlide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(asciidocSlideDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AsciidocSlide in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAsciidocSlide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        asciidocSlide.setId(longCount.incrementAndGet());

        // Create the AsciidocSlide
        AsciidocSlideDTO asciidocSlideDTO = asciidocSlideMapper.toDto(asciidocSlide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(asciidocSlideDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AsciidocSlide in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAsciidocSlide() {
        // Initialize the database
        insertedAsciidocSlide = asciidocSlideRepository.save(asciidocSlide).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the asciidocSlide
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, asciidocSlide.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return asciidocSlideRepository.count().block();
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

    protected AsciidocSlide getPersistedAsciidocSlide(AsciidocSlide asciidocSlide) {
        return asciidocSlideRepository.findById(asciidocSlide.getId()).block();
    }

    protected void assertPersistedAsciidocSlideToMatchAllProperties(AsciidocSlide expectedAsciidocSlide) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAsciidocSlideAllPropertiesEquals(expectedAsciidocSlide, getPersistedAsciidocSlide(expectedAsciidocSlide));
        assertAsciidocSlideUpdatableFieldsEquals(expectedAsciidocSlide, getPersistedAsciidocSlide(expectedAsciidocSlide));
    }

    protected void assertPersistedAsciidocSlideToMatchUpdatableProperties(AsciidocSlide expectedAsciidocSlide) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAsciidocSlideAllUpdatablePropertiesEquals(expectedAsciidocSlide, getPersistedAsciidocSlide(expectedAsciidocSlide));
        assertAsciidocSlideUpdatableFieldsEquals(expectedAsciidocSlide, getPersistedAsciidocSlide(expectedAsciidocSlide));
    }
}
