package edtech.service.impl;

import edtech.repository.WorkspaceRepository;
import edtech.service.WorkspaceService;
import edtech.service.dto.WorkspaceDTO;
import edtech.service.mapper.WorkspaceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link edtech.domain.Workspace}.
 */
@Service
@Transactional
public class WorkspaceServiceImpl implements WorkspaceService {

    private static final Logger LOG = LoggerFactory.getLogger(WorkspaceServiceImpl.class);

    private final WorkspaceRepository workspaceRepository;

    private final WorkspaceMapper workspaceMapper;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository, WorkspaceMapper workspaceMapper) {
        this.workspaceRepository = workspaceRepository;
        this.workspaceMapper = workspaceMapper;
    }

    @Override
    public Mono<WorkspaceDTO> save(WorkspaceDTO workspaceDTO) {
        LOG.debug("Request to save Workspace : {}", workspaceDTO);
        return workspaceRepository.save(workspaceMapper.toEntity(workspaceDTO)).map(workspaceMapper::toDto);
    }

    @Override
    public Mono<WorkspaceDTO> update(WorkspaceDTO workspaceDTO) {
        LOG.debug("Request to update Workspace : {}", workspaceDTO);
        return workspaceRepository.save(workspaceMapper.toEntity(workspaceDTO)).map(workspaceMapper::toDto);
    }

    @Override
    public Mono<WorkspaceDTO> partialUpdate(WorkspaceDTO workspaceDTO) {
        LOG.debug("Request to partially update Workspace : {}", workspaceDTO);

        return workspaceRepository
            .findById(workspaceDTO.getId())
            .map(existingWorkspace -> {
                workspaceMapper.partialUpdate(existingWorkspace, workspaceDTO);

                return existingWorkspace;
            })
            .flatMap(workspaceRepository::save)
            .map(workspaceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<WorkspaceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Workspaces");
        return workspaceRepository.findAllBy(pageable).map(workspaceMapper::toDto);
    }

    public Flux<WorkspaceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return workspaceRepository.findAllWithEagerRelationships(pageable).map(workspaceMapper::toDto);
    }

    public Mono<Long> countAll() {
        return workspaceRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<WorkspaceDTO> findOne(Long id) {
        LOG.debug("Request to get Workspace : {}", id);
        return workspaceRepository.findOneWithEagerRelationships(id).map(workspaceMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Workspace : {}", id);
        return workspaceRepository.deleteById(id);
    }
}
