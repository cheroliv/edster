package edtech.service;

import edtech.service.dto.PlantUMLDiagramResourceDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link edtech.domain.PlantUMLDiagramResource}.
 */
public interface PlantUMLDiagramResourceService {
    /**
     * Save a plantUMLDiagramResource.
     *
     * @param plantUMLDiagramResourceDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PlantUMLDiagramResourceDTO> save(PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO);

    /**
     * Updates a plantUMLDiagramResource.
     *
     * @param plantUMLDiagramResourceDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PlantUMLDiagramResourceDTO> update(PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO);

    /**
     * Partially updates a plantUMLDiagramResource.
     *
     * @param plantUMLDiagramResourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PlantUMLDiagramResourceDTO> partialUpdate(PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO);

    /**
     * Get all the plantUMLDiagramResources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PlantUMLDiagramResourceDTO> findAll(Pageable pageable);

    /**
     * Returns the number of plantUMLDiagramResources available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" plantUMLDiagramResource.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PlantUMLDiagramResourceDTO> findOne(Long id);

    /**
     * Delete the "id" plantUMLDiagramResource.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
