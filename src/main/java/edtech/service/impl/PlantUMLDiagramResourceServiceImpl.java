package edtech.service.impl;

import edtech.repository.PlantUMLDiagramResourceRepository;
import edtech.service.PlantUMLDiagramResourceService;
import edtech.service.dto.PlantUMLDiagramResourceDTO;
import edtech.service.mapper.PlantUMLDiagramResourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link edtech.domain.PlantUMLDiagramResource}.
 */
@Service
@Transactional
public class PlantUMLDiagramResourceServiceImpl implements PlantUMLDiagramResourceService {

    private static final Logger LOG = LoggerFactory.getLogger(PlantUMLDiagramResourceServiceImpl.class);

    private final PlantUMLDiagramResourceRepository plantUMLDiagramResourceRepository;

    private final PlantUMLDiagramResourceMapper plantUMLDiagramResourceMapper;

    public PlantUMLDiagramResourceServiceImpl(
        PlantUMLDiagramResourceRepository plantUMLDiagramResourceRepository,
        PlantUMLDiagramResourceMapper plantUMLDiagramResourceMapper
    ) {
        this.plantUMLDiagramResourceRepository = plantUMLDiagramResourceRepository;
        this.plantUMLDiagramResourceMapper = plantUMLDiagramResourceMapper;
    }

    @Override
    public Mono<PlantUMLDiagramResourceDTO> save(PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO) {
        LOG.debug("Request to save PlantUMLDiagramResource : {}", plantUMLDiagramResourceDTO);
        return plantUMLDiagramResourceRepository
            .save(plantUMLDiagramResourceMapper.toEntity(plantUMLDiagramResourceDTO))
            .map(plantUMLDiagramResourceMapper::toDto);
    }

    @Override
    public Mono<PlantUMLDiagramResourceDTO> update(PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO) {
        LOG.debug("Request to update PlantUMLDiagramResource : {}", plantUMLDiagramResourceDTO);
        return plantUMLDiagramResourceRepository
            .save(plantUMLDiagramResourceMapper.toEntity(plantUMLDiagramResourceDTO))
            .map(plantUMLDiagramResourceMapper::toDto);
    }

    @Override
    public Mono<PlantUMLDiagramResourceDTO> partialUpdate(PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO) {
        LOG.debug("Request to partially update PlantUMLDiagramResource : {}", plantUMLDiagramResourceDTO);

        return plantUMLDiagramResourceRepository
            .findById(plantUMLDiagramResourceDTO.getId())
            .map(existingPlantUMLDiagramResource -> {
                plantUMLDiagramResourceMapper.partialUpdate(existingPlantUMLDiagramResource, plantUMLDiagramResourceDTO);

                return existingPlantUMLDiagramResource;
            })
            .flatMap(plantUMLDiagramResourceRepository::save)
            .map(plantUMLDiagramResourceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PlantUMLDiagramResourceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PlantUMLDiagramResources");
        return plantUMLDiagramResourceRepository.findAllBy(pageable).map(plantUMLDiagramResourceMapper::toDto);
    }

    public Mono<Long> countAll() {
        return plantUMLDiagramResourceRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PlantUMLDiagramResourceDTO> findOne(Long id) {
        LOG.debug("Request to get PlantUMLDiagramResource : {}", id);
        return plantUMLDiagramResourceRepository.findById(id).map(plantUMLDiagramResourceMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete PlantUMLDiagramResource : {}", id);
        return plantUMLDiagramResourceRepository.deleteById(id);
    }
}
