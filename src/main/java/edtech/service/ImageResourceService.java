package edtech.service;

import edtech.service.dto.ImageResourceDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link edtech.domain.ImageResource}.
 */
public interface ImageResourceService {
    /**
     * Save a imageResource.
     *
     * @param imageResourceDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ImageResourceDTO> save(ImageResourceDTO imageResourceDTO);

    /**
     * Updates a imageResource.
     *
     * @param imageResourceDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ImageResourceDTO> update(ImageResourceDTO imageResourceDTO);

    /**
     * Partially updates a imageResource.
     *
     * @param imageResourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ImageResourceDTO> partialUpdate(ImageResourceDTO imageResourceDTO);

    /**
     * Get all the imageResources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ImageResourceDTO> findAll(Pageable pageable);

    /**
     * Returns the number of imageResources available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" imageResource.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ImageResourceDTO> findOne(Long id);

    /**
     * Delete the "id" imageResource.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
