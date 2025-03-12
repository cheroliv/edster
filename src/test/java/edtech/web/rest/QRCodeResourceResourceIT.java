package edtech.web.rest;

import static edtech.domain.QRCodeResourceAsserts.*;
import static edtech.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import edtech.IntegrationTest;
import edtech.domain.QRCodeResource;
import edtech.domain.enumeration.DocumentResourceType;
import edtech.repository.EntityManager;
import edtech.repository.QRCodeResourceRepository;
import edtech.service.dto.QRCodeResourceDTO;
import edtech.service.mapper.QRCodeResourceMapper;
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
 * Integration tests for the {@link QRCodeResourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class QRCodeResourceResourceIT {

    private static final DocumentResourceType DEFAULT_TYPE = DocumentResourceType.IMAGE;
    private static final DocumentResourceType UPDATED_TYPE = DocumentResourceType.LINK;

    private static final String DEFAULT_URI = "AAAAAAAAAA";
    private static final String UPDATED_URI = "BBBBBBBBBB";

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/qr-code-resources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QRCodeResourceRepository qRCodeResourceRepository;

    @Autowired
    private QRCodeResourceMapper qRCodeResourceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private QRCodeResource qRCodeResource;

    private QRCodeResource insertedQRCodeResource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QRCodeResource createEntity() {
        return new QRCodeResource().type(DEFAULT_TYPE).uri(DEFAULT_URI).data(DEFAULT_DATA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QRCodeResource createUpdatedEntity() {
        return new QRCodeResource().type(UPDATED_TYPE).uri(UPDATED_URI).data(UPDATED_DATA);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(QRCodeResource.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        qRCodeResource = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedQRCodeResource != null) {
            qRCodeResourceRepository.delete(insertedQRCodeResource).block();
            insertedQRCodeResource = null;
        }
        deleteEntities(em);
    }

    @Test
    void createQRCodeResource() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QRCodeResource
        QRCodeResourceDTO qRCodeResourceDTO = qRCodeResourceMapper.toDto(qRCodeResource);
        var returnedQRCodeResourceDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(qRCodeResourceDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(QRCodeResourceDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the QRCodeResource in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQRCodeResource = qRCodeResourceMapper.toEntity(returnedQRCodeResourceDTO);
        assertQRCodeResourceUpdatableFieldsEquals(returnedQRCodeResource, getPersistedQRCodeResource(returnedQRCodeResource));

        insertedQRCodeResource = returnedQRCodeResource;
    }

    @Test
    void createQRCodeResourceWithExistingId() throws Exception {
        // Create the QRCodeResource with an existing ID
        qRCodeResource.setId(1L);
        QRCodeResourceDTO qRCodeResourceDTO = qRCodeResourceMapper.toDto(qRCodeResource);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(qRCodeResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the QRCodeResource in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllQRCodeResources() {
        // Initialize the database
        insertedQRCodeResource = qRCodeResourceRepository.save(qRCodeResource).block();

        // Get all the qRCodeResourceList
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
            .value(hasItem(qRCodeResource.getId().intValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()))
            .jsonPath("$.[*].uri")
            .value(hasItem(DEFAULT_URI))
            .jsonPath("$.[*].data")
            .value(hasItem(DEFAULT_DATA));
    }

    @Test
    void getQRCodeResource() {
        // Initialize the database
        insertedQRCodeResource = qRCodeResourceRepository.save(qRCodeResource).block();

        // Get the qRCodeResource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, qRCodeResource.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(qRCodeResource.getId().intValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()))
            .jsonPath("$.uri")
            .value(is(DEFAULT_URI))
            .jsonPath("$.data")
            .value(is(DEFAULT_DATA));
    }

    @Test
    void getNonExistingQRCodeResource() {
        // Get the qRCodeResource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingQRCodeResource() throws Exception {
        // Initialize the database
        insertedQRCodeResource = qRCodeResourceRepository.save(qRCodeResource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the qRCodeResource
        QRCodeResource updatedQRCodeResource = qRCodeResourceRepository.findById(qRCodeResource.getId()).block();
        updatedQRCodeResource.type(UPDATED_TYPE).uri(UPDATED_URI).data(UPDATED_DATA);
        QRCodeResourceDTO qRCodeResourceDTO = qRCodeResourceMapper.toDto(updatedQRCodeResource);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, qRCodeResourceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(qRCodeResourceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the QRCodeResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQRCodeResourceToMatchAllProperties(updatedQRCodeResource);
    }

    @Test
    void putNonExistingQRCodeResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRCodeResource.setId(longCount.incrementAndGet());

        // Create the QRCodeResource
        QRCodeResourceDTO qRCodeResourceDTO = qRCodeResourceMapper.toDto(qRCodeResource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, qRCodeResourceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(qRCodeResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the QRCodeResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchQRCodeResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRCodeResource.setId(longCount.incrementAndGet());

        // Create the QRCodeResource
        QRCodeResourceDTO qRCodeResourceDTO = qRCodeResourceMapper.toDto(qRCodeResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(qRCodeResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the QRCodeResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamQRCodeResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRCodeResource.setId(longCount.incrementAndGet());

        // Create the QRCodeResource
        QRCodeResourceDTO qRCodeResourceDTO = qRCodeResourceMapper.toDto(qRCodeResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(qRCodeResourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the QRCodeResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateQRCodeResourceWithPatch() throws Exception {
        // Initialize the database
        insertedQRCodeResource = qRCodeResourceRepository.save(qRCodeResource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the qRCodeResource using partial update
        QRCodeResource partialUpdatedQRCodeResource = new QRCodeResource();
        partialUpdatedQRCodeResource.setId(qRCodeResource.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedQRCodeResource.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedQRCodeResource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the QRCodeResource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQRCodeResourceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQRCodeResource, qRCodeResource),
            getPersistedQRCodeResource(qRCodeResource)
        );
    }

    @Test
    void fullUpdateQRCodeResourceWithPatch() throws Exception {
        // Initialize the database
        insertedQRCodeResource = qRCodeResourceRepository.save(qRCodeResource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the qRCodeResource using partial update
        QRCodeResource partialUpdatedQRCodeResource = new QRCodeResource();
        partialUpdatedQRCodeResource.setId(qRCodeResource.getId());

        partialUpdatedQRCodeResource.type(UPDATED_TYPE).uri(UPDATED_URI).data(UPDATED_DATA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedQRCodeResource.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedQRCodeResource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the QRCodeResource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQRCodeResourceUpdatableFieldsEquals(partialUpdatedQRCodeResource, getPersistedQRCodeResource(partialUpdatedQRCodeResource));
    }

    @Test
    void patchNonExistingQRCodeResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRCodeResource.setId(longCount.incrementAndGet());

        // Create the QRCodeResource
        QRCodeResourceDTO qRCodeResourceDTO = qRCodeResourceMapper.toDto(qRCodeResource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, qRCodeResourceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(qRCodeResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the QRCodeResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchQRCodeResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRCodeResource.setId(longCount.incrementAndGet());

        // Create the QRCodeResource
        QRCodeResourceDTO qRCodeResourceDTO = qRCodeResourceMapper.toDto(qRCodeResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(qRCodeResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the QRCodeResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamQRCodeResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRCodeResource.setId(longCount.incrementAndGet());

        // Create the QRCodeResource
        QRCodeResourceDTO qRCodeResourceDTO = qRCodeResourceMapper.toDto(qRCodeResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(qRCodeResourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the QRCodeResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteQRCodeResource() {
        // Initialize the database
        insertedQRCodeResource = qRCodeResourceRepository.save(qRCodeResource).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the qRCodeResource
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, qRCodeResource.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return qRCodeResourceRepository.count().block();
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

    protected QRCodeResource getPersistedQRCodeResource(QRCodeResource qRCodeResource) {
        return qRCodeResourceRepository.findById(qRCodeResource.getId()).block();
    }

    protected void assertPersistedQRCodeResourceToMatchAllProperties(QRCodeResource expectedQRCodeResource) {
        // Test fails because reactive api returns an empty object instead of null
        // assertQRCodeResourceAllPropertiesEquals(expectedQRCodeResource, getPersistedQRCodeResource(expectedQRCodeResource));
        assertQRCodeResourceUpdatableFieldsEquals(expectedQRCodeResource, getPersistedQRCodeResource(expectedQRCodeResource));
    }

    protected void assertPersistedQRCodeResourceToMatchUpdatableProperties(QRCodeResource expectedQRCodeResource) {
        // Test fails because reactive api returns an empty object instead of null
        // assertQRCodeResourceAllUpdatablePropertiesEquals(expectedQRCodeResource, getPersistedQRCodeResource(expectedQRCodeResource));
        assertQRCodeResourceUpdatableFieldsEquals(expectedQRCodeResource, getPersistedQRCodeResource(expectedQRCodeResource));
    }
}
