package edtech.service;

import edtech.service.dto.PresentationDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link edtech.domain.Presentation}.
 */
public interface PresentationService {
    /**
     * Save a presentation.
     *
     * @param presentationDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PresentationDTO> save(PresentationDTO presentationDTO);

    /**
     * Updates a presentation.
     *
     * @param presentationDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PresentationDTO> update(PresentationDTO presentationDTO);

    /**
     * Partially updates a presentation.
     *
     * @param presentationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PresentationDTO> partialUpdate(PresentationDTO presentationDTO);

    /**
     * Get all the presentations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PresentationDTO> findAll(Pageable pageable);

    /**
     * Returns the number of presentations available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" presentation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PresentationDTO> findOne(Long id);

    /**
     * Delete the "id" presentation.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
