package edtech.service.impl;

import edtech.repository.LinkResourceRepository;
import edtech.service.LinkResourceService;
import edtech.service.dto.LinkResourceDTO;
import edtech.service.mapper.LinkResourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link edtech.domain.LinkResource}.
 */
@Service
@Transactional
public class LinkResourceServiceImpl implements LinkResourceService {

    private static final Logger LOG = LoggerFactory.getLogger(LinkResourceServiceImpl.class);

    private final LinkResourceRepository linkResourceRepository;

    private final LinkResourceMapper linkResourceMapper;

    public LinkResourceServiceImpl(LinkResourceRepository linkResourceRepository, LinkResourceMapper linkResourceMapper) {
        this.linkResourceRepository = linkResourceRepository;
        this.linkResourceMapper = linkResourceMapper;
    }

    @Override
    public Mono<LinkResourceDTO> save(LinkResourceDTO linkResourceDTO) {
        LOG.debug("Request to save LinkResource : {}", linkResourceDTO);
        return linkResourceRepository.save(linkResourceMapper.toEntity(linkResourceDTO)).map(linkResourceMapper::toDto);
    }

    @Override
    public Mono<LinkResourceDTO> update(LinkResourceDTO linkResourceDTO) {
        LOG.debug("Request to update LinkResource : {}", linkResourceDTO);
        return linkResourceRepository.save(linkResourceMapper.toEntity(linkResourceDTO)).map(linkResourceMapper::toDto);
    }

    @Override
    public Mono<LinkResourceDTO> partialUpdate(LinkResourceDTO linkResourceDTO) {
        LOG.debug("Request to partially update LinkResource : {}", linkResourceDTO);

        return linkResourceRepository
            .findById(linkResourceDTO.getId())
            .map(existingLinkResource -> {
                linkResourceMapper.partialUpdate(existingLinkResource, linkResourceDTO);

                return existingLinkResource;
            })
            .flatMap(linkResourceRepository::save)
            .map(linkResourceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<LinkResourceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all LinkResources");
        return linkResourceRepository.findAllBy(pageable).map(linkResourceMapper::toDto);
    }

    public Mono<Long> countAll() {
        return linkResourceRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<LinkResourceDTO> findOne(Long id) {
        LOG.debug("Request to get LinkResource : {}", id);
        return linkResourceRepository.findById(id).map(linkResourceMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete LinkResource : {}", id);
        return linkResourceRepository.deleteById(id);
    }
}
