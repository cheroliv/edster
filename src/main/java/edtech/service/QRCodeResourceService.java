package edtech.service;

import edtech.service.dto.QRCodeResourceDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link edtech.domain.QRCodeResource}.
 */
public interface QRCodeResourceService {
    /**
     * Save a qRCodeResource.
     *
     * @param qRCodeResourceDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<QRCodeResourceDTO> save(QRCodeResourceDTO qRCodeResourceDTO);

    /**
     * Updates a qRCodeResource.
     *
     * @param qRCodeResourceDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<QRCodeResourceDTO> update(QRCodeResourceDTO qRCodeResourceDTO);

    /**
     * Partially updates a qRCodeResource.
     *
     * @param qRCodeResourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<QRCodeResourceDTO> partialUpdate(QRCodeResourceDTO qRCodeResourceDTO);

    /**
     * Get all the qRCodeResources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<QRCodeResourceDTO> findAll(Pageable pageable);

    /**
     * Returns the number of qRCodeResources available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" qRCodeResource.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<QRCodeResourceDTO> findOne(Long id);

    /**
     * Delete the "id" qRCodeResource.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
