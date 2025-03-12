package edtech.service.impl;

import edtech.repository.AsciidocSlideRepository;
import edtech.service.AsciidocSlideService;
import edtech.service.dto.AsciidocSlideDTO;
import edtech.service.mapper.AsciidocSlideMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link edtech.domain.AsciidocSlide}.
 */
@Service
@Transactional
public class AsciidocSlideServiceImpl implements AsciidocSlideService {

    private static final Logger LOG = LoggerFactory.getLogger(AsciidocSlideServiceImpl.class);

    private final AsciidocSlideRepository asciidocSlideRepository;

    private final AsciidocSlideMapper asciidocSlideMapper;

    public AsciidocSlideServiceImpl(AsciidocSlideRepository asciidocSlideRepository, AsciidocSlideMapper asciidocSlideMapper) {
        this.asciidocSlideRepository = asciidocSlideRepository;
        this.asciidocSlideMapper = asciidocSlideMapper;
    }

    @Override
    public Mono<AsciidocSlideDTO> save(AsciidocSlideDTO asciidocSlideDTO) {
        LOG.debug("Request to save AsciidocSlide : {}", asciidocSlideDTO);
        return asciidocSlideRepository.save(asciidocSlideMapper.toEntity(asciidocSlideDTO)).map(asciidocSlideMapper::toDto);
    }

    @Override
    public Mono<AsciidocSlideDTO> update(AsciidocSlideDTO asciidocSlideDTO) {
        LOG.debug("Request to update AsciidocSlide : {}", asciidocSlideDTO);
        return asciidocSlideRepository.save(asciidocSlideMapper.toEntity(asciidocSlideDTO)).map(asciidocSlideMapper::toDto);
    }

    @Override
    public Mono<AsciidocSlideDTO> partialUpdate(AsciidocSlideDTO asciidocSlideDTO) {
        LOG.debug("Request to partially update AsciidocSlide : {}", asciidocSlideDTO);

        return asciidocSlideRepository
            .findById(asciidocSlideDTO.getId())
            .map(existingAsciidocSlide -> {
                asciidocSlideMapper.partialUpdate(existingAsciidocSlide, asciidocSlideDTO);

                return existingAsciidocSlide;
            })
            .flatMap(asciidocSlideRepository::save)
            .map(asciidocSlideMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AsciidocSlideDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AsciidocSlides");
        return asciidocSlideRepository.findAllBy(pageable).map(asciidocSlideMapper::toDto);
    }

    public Mono<Long> countAll() {
        return asciidocSlideRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AsciidocSlideDTO> findOne(Long id) {
        LOG.debug("Request to get AsciidocSlide : {}", id);
        return asciidocSlideRepository.findById(id).map(asciidocSlideMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete AsciidocSlide : {}", id);
        return asciidocSlideRepository.deleteById(id);
    }
}
