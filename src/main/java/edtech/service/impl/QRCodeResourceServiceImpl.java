package edtech.service.impl;

import edtech.repository.QRCodeResourceRepository;
import edtech.service.QRCodeResourceService;
import edtech.service.dto.QRCodeResourceDTO;
import edtech.service.mapper.QRCodeResourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link edtech.domain.QRCodeResource}.
 */
@Service
@Transactional
public class QRCodeResourceServiceImpl implements QRCodeResourceService {

    private static final Logger LOG = LoggerFactory.getLogger(QRCodeResourceServiceImpl.class);

    private final QRCodeResourceRepository qRCodeResourceRepository;

    private final QRCodeResourceMapper qRCodeResourceMapper;

    public QRCodeResourceServiceImpl(QRCodeResourceRepository qRCodeResourceRepository, QRCodeResourceMapper qRCodeResourceMapper) {
        this.qRCodeResourceRepository = qRCodeResourceRepository;
        this.qRCodeResourceMapper = qRCodeResourceMapper;
    }

    @Override
    public Mono<QRCodeResourceDTO> save(QRCodeResourceDTO qRCodeResourceDTO) {
        LOG.debug("Request to save QRCodeResource : {}", qRCodeResourceDTO);
        return qRCodeResourceRepository.save(qRCodeResourceMapper.toEntity(qRCodeResourceDTO)).map(qRCodeResourceMapper::toDto);
    }

    @Override
    public Mono<QRCodeResourceDTO> update(QRCodeResourceDTO qRCodeResourceDTO) {
        LOG.debug("Request to update QRCodeResource : {}", qRCodeResourceDTO);
        return qRCodeResourceRepository.save(qRCodeResourceMapper.toEntity(qRCodeResourceDTO)).map(qRCodeResourceMapper::toDto);
    }

    @Override
    public Mono<QRCodeResourceDTO> partialUpdate(QRCodeResourceDTO qRCodeResourceDTO) {
        LOG.debug("Request to partially update QRCodeResource : {}", qRCodeResourceDTO);

        return qRCodeResourceRepository
            .findById(qRCodeResourceDTO.getId())
            .map(existingQRCodeResource -> {
                qRCodeResourceMapper.partialUpdate(existingQRCodeResource, qRCodeResourceDTO);

                return existingQRCodeResource;
            })
            .flatMap(qRCodeResourceRepository::save)
            .map(qRCodeResourceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<QRCodeResourceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all QRCodeResources");
        return qRCodeResourceRepository.findAllBy(pageable).map(qRCodeResourceMapper::toDto);
    }

    public Mono<Long> countAll() {
        return qRCodeResourceRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<QRCodeResourceDTO> findOne(Long id) {
        LOG.debug("Request to get QRCodeResource : {}", id);
        return qRCodeResourceRepository.findById(id).map(qRCodeResourceMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete QRCodeResource : {}", id);
        return qRCodeResourceRepository.deleteById(id);
    }
}
