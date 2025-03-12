package edtech.service.impl;

import edtech.repository.ImageResourceRepository;
import edtech.service.ImageResourceService;
import edtech.service.dto.ImageResourceDTO;
import edtech.service.mapper.ImageResourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link edtech.domain.ImageResource}.
 */
@Service
@Transactional
public class ImageResourceServiceImpl implements ImageResourceService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageResourceServiceImpl.class);

    private final ImageResourceRepository imageResourceRepository;

    private final ImageResourceMapper imageResourceMapper;

    public ImageResourceServiceImpl(ImageResourceRepository imageResourceRepository, ImageResourceMapper imageResourceMapper) {
        this.imageResourceRepository = imageResourceRepository;
        this.imageResourceMapper = imageResourceMapper;
    }

    @Override
    public Mono<ImageResourceDTO> save(ImageResourceDTO imageResourceDTO) {
        LOG.debug("Request to save ImageResource : {}", imageResourceDTO);
        return imageResourceRepository.save(imageResourceMapper.toEntity(imageResourceDTO)).map(imageResourceMapper::toDto);
    }

    @Override
    public Mono<ImageResourceDTO> update(ImageResourceDTO imageResourceDTO) {
        LOG.debug("Request to update ImageResource : {}", imageResourceDTO);
        return imageResourceRepository.save(imageResourceMapper.toEntity(imageResourceDTO)).map(imageResourceMapper::toDto);
    }

    @Override
    public Mono<ImageResourceDTO> partialUpdate(ImageResourceDTO imageResourceDTO) {
        LOG.debug("Request to partially update ImageResource : {}", imageResourceDTO);

        return imageResourceRepository
            .findById(imageResourceDTO.getId())
            .map(existingImageResource -> {
                imageResourceMapper.partialUpdate(existingImageResource, imageResourceDTO);

                return existingImageResource;
            })
            .flatMap(imageResourceRepository::save)
            .map(imageResourceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ImageResourceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ImageResources");
        return imageResourceRepository.findAllBy(pageable).map(imageResourceMapper::toDto);
    }

    public Mono<Long> countAll() {
        return imageResourceRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ImageResourceDTO> findOne(Long id) {
        LOG.debug("Request to get ImageResource : {}", id);
        return imageResourceRepository.findById(id).map(imageResourceMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete ImageResource : {}", id);
        return imageResourceRepository.deleteById(id);
    }
}
