package edtech.web.rest;

import static edtech.domain.ImageResourceAsserts.*;
import static edtech.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import edtech.IntegrationTest;
import edtech.domain.ImageResource;
import edtech.domain.enumeration.DocumentResourceType;
import edtech.repository.EntityManager;
import edtech.repository.ImageResourceRepository;
import edtech.service.dto.ImageResourceDTO;
import edtech.service.mapper.ImageResourceMapper;
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
 * Integration tests for the {@link ImageResourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ImageResourceResourceIT {

    private static final DocumentResourceType DEFAULT_TYPE = DocumentResourceType.IMAGE;
    private static final DocumentResourceType UPDATED_TYPE = DocumentResourceType.LINK;

    private static final String DEFAULT_URI = "AAAAAAAAAA";
    private static final String UPDATED_URI = "BBBBBBBBBB";

    private static final String DEFAULT_RESOLUTION = "AAAAAAAAAA";
    private static final String UPDATED_RESOLUTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/image-resources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ImageResourceRepository imageResourceRepository;

    @Autowired
    private ImageResourceMapper imageResourceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ImageResource imageResource;

    private ImageResource insertedImageResource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageResource createEntity() {
        return new ImageResource().type(DEFAULT_TYPE).uri(DEFAULT_URI).resolution(DEFAULT_RESOLUTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageResource createUpdatedEntity() {
        return new ImageResource().type(UPDATED_TYPE).uri(UPDATED_URI).resolution(UPDATED_RESOLUTION);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ImageResource.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        imageResource = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedImageResource != null) {
            imageResourceRepository.delete(insertedImageResource).block();
            insertedImageResource = null;
        }
        deleteEntities(em);
    }

    @Test
    void createImageResource() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ImageResource
        ImageResourceDTO imageResourceDTO = imageResourceMapper.toDto(imageResource);
        var returnedImageResourceDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(imageResourceDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ImageResourceDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the ImageResource in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedImageResource = imageResourceMapper.toEntity(returnedImageResourceDTO);
        assertImageResourceUpdatableFieldsEquals(returnedImageResource, getPersistedImageResource(returnedImageResource));

        insertedImageResource = returnedImageResource;
    }

    @Test
    void createImageResourceWithExistingId() throws Exception {
        // Create the ImageResource with an existing ID
        imageResource.setId(1L);
        ImageResourceDTO imageResourceDTO = imageResourceMapper.toDto(imageResource);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(imageResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ImageResource in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllImageResources() {
        // Initialize the database
        insertedImageResource = imageResourceRepository.save(imageResource).block();

        // Get all the imageResourceList
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
            .value(hasItem(imageResource.getId().intValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()))
            .jsonPath("$.[*].uri")
            .value(hasItem(DEFAULT_URI))
            .jsonPath("$.[*].resolution")
            .value(hasItem(DEFAULT_RESOLUTION));
    }

    @Test
    void getImageResource() {
        // Initialize the database
        insertedImageResource = imageResourceRepository.save(imageResource).block();

        // Get the imageResource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, imageResource.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(imageResource.getId().intValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()))
            .jsonPath("$.uri")
            .value(is(DEFAULT_URI))
            .jsonPath("$.resolution")
            .value(is(DEFAULT_RESOLUTION));
    }

    @Test
    void getNonExistingImageResource() {
        // Get the imageResource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingImageResource() throws Exception {
        // Initialize the database
        insertedImageResource = imageResourceRepository.save(imageResource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imageResource
        ImageResource updatedImageResource = imageResourceRepository.findById(imageResource.getId()).block();
        updatedImageResource.type(UPDATED_TYPE).uri(UPDATED_URI).resolution(UPDATED_RESOLUTION);
        ImageResourceDTO imageResourceDTO = imageResourceMapper.toDto(updatedImageResource);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, imageResourceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(imageResourceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ImageResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedImageResourceToMatchAllProperties(updatedImageResource);
    }

    @Test
    void putNonExistingImageResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imageResource.setId(longCount.incrementAndGet());

        // Create the ImageResource
        ImageResourceDTO imageResourceDTO = imageResourceMapper.toDto(imageResource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, imageResourceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(imageResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ImageResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchImageResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imageResource.setId(longCount.incrementAndGet());

        // Create the ImageResource
        ImageResourceDTO imageResourceDTO = imageResourceMapper.toDto(imageResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(imageResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ImageResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamImageResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imageResource.setId(longCount.incrementAndGet());

        // Create the ImageResource
        ImageResourceDTO imageResourceDTO = imageResourceMapper.toDto(imageResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(imageResourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ImageResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateImageResourceWithPatch() throws Exception {
        // Initialize the database
        insertedImageResource = imageResourceRepository.save(imageResource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imageResource using partial update
        ImageResource partialUpdatedImageResource = new ImageResource();
        partialUpdatedImageResource.setId(imageResource.getId());

        partialUpdatedImageResource.type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedImageResource.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedImageResource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ImageResource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageResourceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedImageResource, imageResource),
            getPersistedImageResource(imageResource)
        );
    }

    @Test
    void fullUpdateImageResourceWithPatch() throws Exception {
        // Initialize the database
        insertedImageResource = imageResourceRepository.save(imageResource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imageResource using partial update
        ImageResource partialUpdatedImageResource = new ImageResource();
        partialUpdatedImageResource.setId(imageResource.getId());

        partialUpdatedImageResource.type(UPDATED_TYPE).uri(UPDATED_URI).resolution(UPDATED_RESOLUTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedImageResource.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedImageResource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ImageResource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageResourceUpdatableFieldsEquals(partialUpdatedImageResource, getPersistedImageResource(partialUpdatedImageResource));
    }

    @Test
    void patchNonExistingImageResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imageResource.setId(longCount.incrementAndGet());

        // Create the ImageResource
        ImageResourceDTO imageResourceDTO = imageResourceMapper.toDto(imageResource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, imageResourceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(imageResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ImageResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchImageResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imageResource.setId(longCount.incrementAndGet());

        // Create the ImageResource
        ImageResourceDTO imageResourceDTO = imageResourceMapper.toDto(imageResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(imageResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ImageResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamImageResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imageResource.setId(longCount.incrementAndGet());

        // Create the ImageResource
        ImageResourceDTO imageResourceDTO = imageResourceMapper.toDto(imageResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(imageResourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ImageResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteImageResource() {
        // Initialize the database
        insertedImageResource = imageResourceRepository.save(imageResource).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the imageResource
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, imageResource.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return imageResourceRepository.count().block();
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

    protected ImageResource getPersistedImageResource(ImageResource imageResource) {
        return imageResourceRepository.findById(imageResource.getId()).block();
    }

    protected void assertPersistedImageResourceToMatchAllProperties(ImageResource expectedImageResource) {
        // Test fails because reactive api returns an empty object instead of null
        // assertImageResourceAllPropertiesEquals(expectedImageResource, getPersistedImageResource(expectedImageResource));
        assertImageResourceUpdatableFieldsEquals(expectedImageResource, getPersistedImageResource(expectedImageResource));
    }

    protected void assertPersistedImageResourceToMatchUpdatableProperties(ImageResource expectedImageResource) {
        // Test fails because reactive api returns an empty object instead of null
        // assertImageResourceAllUpdatablePropertiesEquals(expectedImageResource, getPersistedImageResource(expectedImageResource));
        assertImageResourceUpdatableFieldsEquals(expectedImageResource, getPersistedImageResource(expectedImageResource));
    }
}
