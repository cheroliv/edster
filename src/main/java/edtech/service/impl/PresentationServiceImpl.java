package edtech.service.impl;

import edtech.repository.PresentationRepository;
import edtech.service.PresentationService;
import edtech.service.dto.PresentationDTO;
import edtech.service.mapper.PresentationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link edtech.domain.Presentation}.
 */
@Service
@Transactional
public class PresentationServiceImpl implements PresentationService {

    private static final Logger LOG = LoggerFactory.getLogger(PresentationServiceImpl.class);

    private final PresentationRepository presentationRepository;

    private final PresentationMapper presentationMapper;

    public PresentationServiceImpl(PresentationRepository presentationRepository, PresentationMapper presentationMapper) {
        this.presentationRepository = presentationRepository;
        this.presentationMapper = presentationMapper;
    }

    @Override
    public Mono<PresentationDTO> save(PresentationDTO presentationDTO) {
        LOG.debug("Request to save Presentation : {}", presentationDTO);
        return presentationRepository.save(presentationMapper.toEntity(presentationDTO)).map(presentationMapper::toDto);
    }

    @Override
    public Mono<PresentationDTO> update(PresentationDTO presentationDTO) {
        LOG.debug("Request to update Presentation : {}", presentationDTO);
        return presentationRepository.save(presentationMapper.toEntity(presentationDTO)).map(presentationMapper::toDto);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public Flux<PresentationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Presentations");
        return presentationRepository.findAllBy(pageable).map(presentationMapper::toDto);
    }

    public Mono<Long> countAll() {
        return presentationRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PresentationDTO> findOne(Long id) {
        LOG.debug("Request to get Presentation : {}", id);
        return presentationRepository.findById(id).map(presentationMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Presentation : {}", id);
        return presentationRepository.deleteById(id);
    }
}
