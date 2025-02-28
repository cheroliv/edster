package education.cccp.edtech.service;

import education.cccp.edtech.domain.criteria.PresentationCriteria;
import education.cccp.edtech.repository.PresentationRepository;
import education.cccp.edtech.service.dto.PresentationDTO;
import education.cccp.edtech.service.mapper.PresentationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link education.cccp.edtech.domain.Presentation}.
 */
@Service
@Transactional
public class PresentationService {

    private static final Logger LOG = LoggerFactory.getLogger(PresentationService.class);

    private final PresentationRepository presentationRepository;

    private final PresentationMapper presentationMapper;

    public PresentationService(PresentationRepository presentationRepository, PresentationMapper presentationMapper) {
        this.presentationRepository = presentationRepository;
        this.presentationMapper = presentationMapper;
    }

    /**
     * Save a presentation.
     *
     * @param presentationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PresentationDTO> save(PresentationDTO presentationDTO) {
        LOG.debug("Request to save Presentation : {}", presentationDTO);
        return presentationRepository.save(presentationMapper.toEntity(presentationDTO)).map(presentationMapper::toDto);
    }

    /**
     * Update a presentation.
     *
     * @param presentationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PresentationDTO> update(PresentationDTO presentationDTO) {
        LOG.debug("Request to update Presentation : {}", presentationDTO);
        return presentationRepository.save(presentationMapper.toEntity(presentationDTO)).map(presentationMapper::toDto);
    }

    /**
     * Partially update a presentation.
     *
     * @param presentationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PresentationDTO> partialUpdate(PresentationDTO presentationDTO) {
        LOG.debug("Request to partially update Presentation : {}", presentationDTO);

        return presentationRepository
            .findById(presentationDTO.getId())
            .map(existingPresentation -> {
                presentationMapper.partialUpdate(existingPresentation, presentationDTO);

                return existingPresentation;
            })
            .flatMap(presentationRepository::save)
            .map(presentationMapper::toDto);
    }

    /**
     * Find presentations by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PresentationDTO> findByCriteria(PresentationCriteria criteria, Pageable pageable) {
        LOG.debug("Request to get all Presentations by Criteria");
        return presentationRepository.findByCriteria(criteria, pageable).map(presentationMapper::toDto);
    }

    /**
     * Find the count of presentations by criteria.
     * @param criteria filtering criteria
     * @return the count of presentations
     */
    public Mono<Long> countByCriteria(PresentationCriteria criteria) {
        LOG.debug("Request to get the count of all Presentations by Criteria");
        return presentationRepository.countByCriteria(criteria);
    }

    /**
     * Get all the presentations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<PresentationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return presentationRepository.findAllWithEagerRelationships(pageable).map(presentationMapper::toDto);
    }

    /**
     * Returns the number of presentations available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return presentationRepository.count();
    }

    /**
     * Get one presentation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PresentationDTO> findOne(Long id) {
        LOG.debug("Request to get Presentation : {}", id);
        return presentationRepository.findOneWithEagerRelationships(id).map(presentationMapper::toDto);
    }

    /**
     * Delete the presentation by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Presentation : {}", id);
        return presentationRepository.deleteById(id);
    }
}
