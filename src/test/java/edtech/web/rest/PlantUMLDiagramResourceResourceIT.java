package edtech.web.rest;

import static edtech.domain.PlantUMLDiagramResourceAsserts.*;
import static edtech.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import edtech.IntegrationTest;
import edtech.domain.PlantUMLDiagramResource;
import edtech.domain.enumeration.DocumentResourceType;
import edtech.repository.EntityManager;
import edtech.repository.PlantUMLDiagramResourceRepository;
import edtech.service.dto.PlantUMLDiagramResourceDTO;
import edtech.service.mapper.PlantUMLDiagramResourceMapper;
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
 * Integration tests for the {@link PlantUMLDiagramResourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PlantUMLDiagramResourceResourceIT {

    private static final DocumentResourceType DEFAULT_TYPE = DocumentResourceType.IMAGE;
    private static final DocumentResourceType UPDATED_TYPE = DocumentResourceType.LINK;

    private static final String DEFAULT_URI = "AAAAAAAAAA";
    private static final String UPDATED_URI = "BBBBBBBBBB";

    private static final String DEFAULT_UML_CODE = "AAAAAAAAAA";
    private static final String UPDATED_UML_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/plant-uml-diagram-resources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlantUMLDiagramResourceRepository plantUMLDiagramResourceRepository;

    @Autowired
    private PlantUMLDiagramResourceMapper plantUMLDiagramResourceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PlantUMLDiagramResource plantUMLDiagramResource;

    private PlantUMLDiagramResource insertedPlantUMLDiagramResource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlantUMLDiagramResource createEntity() {
        return new PlantUMLDiagramResource().type(DEFAULT_TYPE).uri(DEFAULT_URI).umlCode(DEFAULT_UML_CODE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlantUMLDiagramResource createUpdatedEntity() {
        return new PlantUMLDiagramResource().type(UPDATED_TYPE).uri(UPDATED_URI).umlCode(UPDATED_UML_CODE);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PlantUMLDiagramResource.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        plantUMLDiagramResource = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPlantUMLDiagramResource != null) {
            plantUMLDiagramResourceRepository.delete(insertedPlantUMLDiagramResource).block();
            insertedPlantUMLDiagramResource = null;
        }
        deleteEntities(em);
    }

    @Test
    void createPlantUMLDiagramResource() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PlantUMLDiagramResource
        PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO = plantUMLDiagramResourceMapper.toDto(plantUMLDiagramResource);
        var returnedPlantUMLDiagramResourceDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(plantUMLDiagramResourceDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(PlantUMLDiagramResourceDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the PlantUMLDiagramResource in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPlantUMLDiagramResource = plantUMLDiagramResourceMapper.toEntity(returnedPlantUMLDiagramResourceDTO);
        assertPlantUMLDiagramResourceUpdatableFieldsEquals(
            returnedPlantUMLDiagramResource,
            getPersistedPlantUMLDiagramResource(returnedPlantUMLDiagramResource)
        );

        insertedPlantUMLDiagramResource = returnedPlantUMLDiagramResource;
    }

    @Test
    void createPlantUMLDiagramResourceWithExistingId() throws Exception {
        // Create the PlantUMLDiagramResource with an existing ID
        plantUMLDiagramResource.setId(1L);
        PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO = plantUMLDiagramResourceMapper.toDto(plantUMLDiagramResource);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(plantUMLDiagramResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PlantUMLDiagramResource in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPlantUMLDiagramResources() {
        // Initialize the database
        insertedPlantUMLDiagramResource = plantUMLDiagramResourceRepository.save(plantUMLDiagramResource).block();

        // Get all the plantUMLDiagramResourceList
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
            .value(hasItem(plantUMLDiagramResource.getId().intValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()))
            .jsonPath("$.[*].uri")
            .value(hasItem(DEFAULT_URI))
            .jsonPath("$.[*].umlCode")
            .value(hasItem(DEFAULT_UML_CODE));
    }

    @Test
    void getPlantUMLDiagramResource() {
        // Initialize the database
        insertedPlantUMLDiagramResource = plantUMLDiagramResourceRepository.save(plantUMLDiagramResource).block();

        // Get the plantUMLDiagramResource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, plantUMLDiagramResource.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(plantUMLDiagramResource.getId().intValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()))
            .jsonPath("$.uri")
            .value(is(DEFAULT_URI))
            .jsonPath("$.umlCode")
            .value(is(DEFAULT_UML_CODE));
    }

    @Test
    void getNonExistingPlantUMLDiagramResource() {
        // Get the plantUMLDiagramResource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPlantUMLDiagramResource() throws Exception {
        // Initialize the database
        insertedPlantUMLDiagramResource = plantUMLDiagramResourceRepository.save(plantUMLDiagramResource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the plantUMLDiagramResource
        PlantUMLDiagramResource updatedPlantUMLDiagramResource = plantUMLDiagramResourceRepository
            .findById(plantUMLDiagramResource.getId())
            .block();
        updatedPlantUMLDiagramResource.type(UPDATED_TYPE).uri(UPDATED_URI).umlCode(UPDATED_UML_CODE);
        PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO = plantUMLDiagramResourceMapper.toDto(updatedPlantUMLDiagramResource);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, plantUMLDiagramResourceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(plantUMLDiagramResourceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PlantUMLDiagramResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlantUMLDiagramResourceToMatchAllProperties(updatedPlantUMLDiagramResource);
    }

    @Test
    void putNonExistingPlantUMLDiagramResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plantUMLDiagramResource.setId(longCount.incrementAndGet());

        // Create the PlantUMLDiagramResource
        PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO = plantUMLDiagramResourceMapper.toDto(plantUMLDiagramResource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, plantUMLDiagramResourceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(plantUMLDiagramResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PlantUMLDiagramResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPlantUMLDiagramResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plantUMLDiagramResource.setId(longCount.incrementAndGet());

        // Create the PlantUMLDiagramResource
        PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO = plantUMLDiagramResourceMapper.toDto(plantUMLDiagramResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(plantUMLDiagramResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PlantUMLDiagramResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPlantUMLDiagramResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plantUMLDiagramResource.setId(longCount.incrementAndGet());

        // Create the PlantUMLDiagramResource
        PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO = plantUMLDiagramResourceMapper.toDto(plantUMLDiagramResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(plantUMLDiagramResourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PlantUMLDiagramResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePlantUMLDiagramResourceWithPatch() throws Exception {
        // Initialize the database
        insertedPlantUMLDiagramResource = plantUMLDiagramResourceRepository.save(plantUMLDiagramResource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the plantUMLDiagramResource using partial update
        PlantUMLDiagramResource partialUpdatedPlantUMLDiagramResource = new PlantUMLDiagramResource();
        partialUpdatedPlantUMLDiagramResource.setId(plantUMLDiagramResource.getId());

        partialUpdatedPlantUMLDiagramResource.uri(UPDATED_URI);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlantUMLDiagramResource.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPlantUMLDiagramResource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PlantUMLDiagramResource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlantUMLDiagramResourceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPlantUMLDiagramResource, plantUMLDiagramResource),
            getPersistedPlantUMLDiagramResource(plantUMLDiagramResource)
        );
    }

    @Test
    void fullUpdatePlantUMLDiagramResourceWithPatch() throws Exception {
        // Initialize the database
        insertedPlantUMLDiagramResource = plantUMLDiagramResourceRepository.save(plantUMLDiagramResource).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the plantUMLDiagramResource using partial update
        PlantUMLDiagramResource partialUpdatedPlantUMLDiagramResource = new PlantUMLDiagramResource();
        partialUpdatedPlantUMLDiagramResource.setId(plantUMLDiagramResource.getId());

        partialUpdatedPlantUMLDiagramResource.type(UPDATED_TYPE).uri(UPDATED_URI).umlCode(UPDATED_UML_CODE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlantUMLDiagramResource.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPlantUMLDiagramResource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PlantUMLDiagramResource in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlantUMLDiagramResourceUpdatableFieldsEquals(
            partialUpdatedPlantUMLDiagramResource,
            getPersistedPlantUMLDiagramResource(partialUpdatedPlantUMLDiagramResource)
        );
    }

    @Test
    void patchNonExistingPlantUMLDiagramResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plantUMLDiagramResource.setId(longCount.incrementAndGet());

        // Create the PlantUMLDiagramResource
        PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO = plantUMLDiagramResourceMapper.toDto(plantUMLDiagramResource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, plantUMLDiagramResourceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(plantUMLDiagramResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PlantUMLDiagramResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPlantUMLDiagramResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plantUMLDiagramResource.setId(longCount.incrementAndGet());

        // Create the PlantUMLDiagramResource
        PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO = plantUMLDiagramResourceMapper.toDto(plantUMLDiagramResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(plantUMLDiagramResourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PlantUMLDiagramResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPlantUMLDiagramResource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plantUMLDiagramResource.setId(longCount.incrementAndGet());

        // Create the PlantUMLDiagramResource
        PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO = plantUMLDiagramResourceMapper.toDto(plantUMLDiagramResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(plantUMLDiagramResourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PlantUMLDiagramResource in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePlantUMLDiagramResource() {
        // Initialize the database
        insertedPlantUMLDiagramResource = plantUMLDiagramResourceRepository.save(plantUMLDiagramResource).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the plantUMLDiagramResource
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, plantUMLDiagramResource.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return plantUMLDiagramResourceRepository.count().block();
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

    protected PlantUMLDiagramResource getPersistedPlantUMLDiagramResource(PlantUMLDiagramResource plantUMLDiagramResource) {
        return plantUMLDiagramResourceRepository.findById(plantUMLDiagramResource.getId()).block();
    }

    protected void assertPersistedPlantUMLDiagramResourceToMatchAllProperties(PlantUMLDiagramResource expectedPlantUMLDiagramResource) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPlantUMLDiagramResourceAllPropertiesEquals(expectedPlantUMLDiagramResource, getPersistedPlantUMLDiagramResource(expectedPlantUMLDiagramResource));
        assertPlantUMLDiagramResourceUpdatableFieldsEquals(
            expectedPlantUMLDiagramResource,
            getPersistedPlantUMLDiagramResource(expectedPlantUMLDiagramResource)
        );
    }

    protected void assertPersistedPlantUMLDiagramResourceToMatchUpdatableProperties(
        PlantUMLDiagramResource expectedPlantUMLDiagramResource
    ) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPlantUMLDiagramResourceAllUpdatablePropertiesEquals(expectedPlantUMLDiagramResource, getPersistedPlantUMLDiagramResource(expectedPlantUMLDiagramResource));
        assertPlantUMLDiagramResourceUpdatableFieldsEquals(
            expectedPlantUMLDiagramResource,
            getPersistedPlantUMLDiagramResource(expectedPlantUMLDiagramResource)
        );
    }
}
