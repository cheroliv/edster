package edtech.service;

import edtech.service.dto.AsciidocSlideDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link edtech.domain.AsciidocSlide}.
 */
public interface AsciidocSlideService {
    /**
     * Save a asciidocSlide.
     *
     * @param asciidocSlideDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AsciidocSlideDTO> save(AsciidocSlideDTO asciidocSlideDTO);

    /**
     * Updates a asciidocSlide.
     *
     * @param asciidocSlideDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AsciidocSlideDTO> update(AsciidocSlideDTO asciidocSlideDTO);

    /**
     * Partially updates a asciidocSlide.
     *
     * @param asciidocSlideDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AsciidocSlideDTO> partialUpdate(AsciidocSlideDTO asciidocSlideDTO);

    /**
     * Get all the asciidocSlides.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AsciidocSlideDTO> findAll(Pageable pageable);

    /**
     * Returns the number of asciidocSlides available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" asciidocSlide.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AsciidocSlideDTO> findOne(Long id);

    /**
     * Delete the "id" asciidocSlide.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
