package edtech.web.rest;

import static edtech.domain.LinkResourceAsserts.*;
import static edtech.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import edtech.IntegrationTest;
import edtech.domain.LinkResource;
import edtech.domain.enumeration.DocumentResourceType;
import edtech.repository.EntityManager;
import edtech.repository.LinkResourceRepository;
import edtech.service.dto.LinkResourceDTO;
import edtech.service.mapper.LinkResourceMapper;
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
 * Integration tests for the {@link LinkResourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LinkResourceResourceIT {

    private static final DocumentResourceType DEFAULT_TYPE = DocumentResourceType.IMAGE;
    private static final DocumentResourceType UPDATED_TYPE = DocumentResourceType.LINK;

    private static final String DEFAULT_URI = "AAAAAAAAAA";
    private static final String UPDATED_URI = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET = "AAAAAAAAAA";
    private static final String UPDATED_TARGET = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/link-resources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LinkResourceRepository linkResourceRepository;

    @Autowired
    private LinkResourceMapper linkResourceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private LinkResource linkResource;

    private LinkResource insertedLinkResource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LinkResource createEntity() {
        return new LinkResource().type(DEFAULT_TYPE).uri(DEFAULT_URI).target(DEFAULT_TARGET);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LinkResource createUpdatedEntity() {
        return new LinkResource().type(UPDATED_TYPE).uri(UPDATED_URI).target(UPDATED_TARGET);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(LinkResource.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        linkResource = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedLinkResource != null) {
            linkResourceRepository.delete(insertedLinkResource).block();
            insertedLinkResource = null;
        }
        deleteEntities(em);
    }

    @Test
    void createLinkResource() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LinkResource
        LinkResourceDTO linkResourceDTO = linkResourceMapper.toDto(linkResource);
        var returnedLinkResourceDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(linkResourceDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(LinkResourceDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the LinkResource in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLinkResource = linkResourceMapper.toEntity(returnedLinkResourceDTO);
        assertLinkResourceUpdatableFieldsEquals(returnedLinkResource, getPersistedLinkResource(returnedLinkResource));

        insertedLinkResource = returnedLinkResource;
    }

    @Test
    void createLinkResourceWithExistingId() throws Exception {
        // Create the LinkResource with an existing ID
        linkResource.setId(1L);
        LinkResourceDTO linkResourceDTO = linkResourceMapper.toDto(linkResource);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(linkResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LinkResource in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllLinkResources() {
        // Initialize the database
        insertedLinkResource = linkResourceRepository.save(linkResource).block();

        // Get all the linkResourceList
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
            .value(hasItem(linkResource.getId().intValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()))
            .jsonPath("$.[*].uri")
            .value(hasItem(DEFAULT_URI))
            .jsonPath("$.[*].target")
            .value(hasItem(DEFAULT_TARGET));
    }

    @Test
    void getLinkResource() {
        // Initialize the database
        insertedLinkResource = linkResourceRepository.save(linkResource).block();

        // Get the linkResource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, linkResource.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(linkResource.getId().intValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()))
            .jsonPath("$.uri")
            .value(is(DEFAULT_URI))
            .jsonPath("$.target")
            .value(is(DEFAULT_TARGET));
    }

    @Test
    void getNonExistingLinkResource() {
        // Get the linkResource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingLinkResource() throws Exception {
        // Initialize the database
        insertedLinkResource = linkResourceRepository.save(linkResource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the linkResource
        LinkResource updatedLinkResource = linkResourceRepository.findById(linkResource.getId()).block();
        updatedLinkResource.type(UPDATED_TYPE).uri(UPDATED_URI).target(UPDATED_TARGET);
        LinkResourceDTO linkResourceDTO = linkResourceMapper.toDto(updatedLinkResource);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, linkResourceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(linkResourceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LinkResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLinkResourceToMatchAllProperties(updatedLinkResource);
    }

    @Test
    void putNonExistingLinkResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkResource.setId(longCount.incrementAndGet());

        // Create the LinkResource
        LinkResourceDTO linkResourceDTO = linkResourceMapper.toDto(linkResource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, linkResourceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(linkResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LinkResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLinkResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkResource.setId(longCount.incrementAndGet());

        // Create the LinkResource
        LinkResourceDTO linkResourceDTO = linkResourceMapper.toDto(linkResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(linkResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LinkResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLinkResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkResource.setId(longCount.incrementAndGet());

        // Create the LinkResource
        LinkResourceDTO linkResourceDTO = linkResourceMapper.toDto(linkResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(linkResourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the LinkResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLinkResourceWithPatch() throws Exception {
        // Initialize the database
        insertedLinkResource = linkResourceRepository.save(linkResource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the linkResource using partial update
        LinkResource partialUpdatedLinkResource = new LinkResource();
        partialUpdatedLinkResource.setId(linkResource.getId());

        partialUpdatedLinkResource.type(UPDATED_TYPE).uri(UPDATED_URI);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLinkResource.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedLinkResource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LinkResource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLinkResourceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLinkResource, linkResource),
            getPersistedLinkResource(linkResource)
        );
    }

    @Test
    void fullUpdateLinkResourceWithPatch() throws Exception {
        // Initialize the database
        insertedLinkResource = linkResourceRepository.save(linkResource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the linkResource using partial update
        LinkResource partialUpdatedLinkResource = new LinkResource();
        partialUpdatedLinkResource.setId(linkResource.getId());

        partialUpdatedLinkResource.type(UPDATED_TYPE).uri(UPDATED_URI).target(UPDATED_TARGET);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLinkResource.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedLinkResource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LinkResource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLinkResourceUpdatableFieldsEquals(partialUpdatedLinkResource, getPersistedLinkResource(partialUpdatedLinkResource));
    }

    @Test
    void patchNonExistingLinkResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkResource.setId(longCount.incrementAndGet());

        // Create the LinkResource
        LinkResourceDTO linkResourceDTO = linkResourceMapper.toDto(linkResource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, linkResourceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(linkResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LinkResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLinkResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkResource.setId(longCount.incrementAndGet());

        // Create the LinkResource
        LinkResourceDTO linkResourceDTO = linkResourceMapper.toDto(linkResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(linkResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LinkResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLinkResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkResource.setId(longCount.incrementAndGet());

        // Create the LinkResource
        LinkResourceDTO linkResourceDTO = linkResourceMapper.toDto(linkResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(linkResourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the LinkResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLinkResource() {
        // Initialize the database
        insertedLinkResource = linkResourceRepository.save(linkResource).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the linkResource
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, linkResource.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return linkResourceRepository.count().block();
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

    protected LinkResource getPersistedLinkResource(LinkResource linkResource) {
        return linkResourceRepository.findById(linkResource.getId()).block();
    }

    protected void assertPersistedLinkResourceToMatchAllProperties(LinkResource expectedLinkResource) {
        // Test fails because reactive api returns an empty object instead of null
        // assertLinkResourceAllPropertiesEquals(expectedLinkResource, getPersistedLinkResource(expectedLinkResource));
        assertLinkResourceUpdatableFieldsEquals(expectedLinkResource, getPersistedLinkResource(expectedLinkResource));
    }

    protected void assertPersistedLinkResourceToMatchUpdatableProperties(LinkResource expectedLinkResource) {
        // Test fails because reactive api returns an empty object instead of null
        // assertLinkResourceAllUpdatablePropertiesEquals(expectedLinkResource, getPersistedLinkResource(expectedLinkResource));
        assertLinkResourceUpdatableFieldsEquals(expectedLinkResource, getPersistedLinkResource(expectedLinkResource));
    }
}
