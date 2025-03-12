package edtech.service;

import edtech.service.dto.LinkResourceDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link edtech.domain.LinkResource}.
 */
public interface LinkResourceService {
    /**
     * Save a linkResource.
     *
     * @param linkResourceDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<LinkResourceDTO> save(LinkResourceDTO linkResourceDTO);

    /**
     * Updates a linkResource.
     *
     * @param linkResourceDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<LinkResourceDTO> update(LinkResourceDTO linkResourceDTO);

    /**
     * Partially updates a linkResource.
     *
     * @param linkResourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<LinkResourceDTO> partialUpdate(LinkResourceDTO linkResourceDTO);

    /**
     * Get all the linkResources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<LinkResourceDTO> findAll(Pageable pageable);

    /**
     * Returns the number of linkResources available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" linkResource.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<LinkResourceDTO> findOne(Long id);

    /**
     * Delete the "id" linkResource.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
