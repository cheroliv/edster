package edtech.service;

import edtech.service.dto.WorkspaceDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link edtech.domain.Workspace}.
 */
public interface WorkspaceService {
    /**
     * Save a workspace.
     *
     * @param workspaceDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<WorkspaceDTO> save(WorkspaceDTO workspaceDTO);

    /**
     * Updates a workspace.
     *
     * @param workspaceDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<WorkspaceDTO> update(WorkspaceDTO workspaceDTO);

    /**
     * Partially updates a workspace.
     *
     * @param workspaceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<WorkspaceDTO> partialUpdate(WorkspaceDTO workspaceDTO);

    /**
     * Get all the workspaces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<WorkspaceDTO> findAll(Pageable pageable);

    /**
     * Get all the workspaces with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<WorkspaceDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of workspaces available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" workspace.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<WorkspaceDTO> findOne(Long id);

    /**
     * Delete the "id" workspace.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
