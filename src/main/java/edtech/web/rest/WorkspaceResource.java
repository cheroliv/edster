package edtech.web.rest;

import edtech.repository.WorkspaceRepository;
import edtech.service.WorkspaceService;
import edtech.service.dto.WorkspaceDTO;
import edtech.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link edtech.domain.Workspace}.
 */
@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceResource {

    private static final Logger LOG = LoggerFactory.getLogger(WorkspaceResource.class);

    private static final String ENTITY_NAME = "workspace";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkspaceService workspaceService;

    private final WorkspaceRepository workspaceRepository;

    public WorkspaceResource(WorkspaceService workspaceService, WorkspaceRepository workspaceRepository) {
        this.workspaceService = workspaceService;
        this.workspaceRepository = workspaceRepository;
    }

    /**
     * {@code POST  /workspaces} : Create a new workspace.
     *
     * @param workspaceDTO the workspaceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workspaceDTO, or with status {@code 400 (Bad Request)} if the workspace has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<WorkspaceDTO>> createWorkspace(@Valid @RequestBody WorkspaceDTO workspaceDTO) throws URISyntaxException {
        LOG.debug("REST request to save Workspace : {}", workspaceDTO);
        if (workspaceDTO.getId() != null) {
            throw new BadRequestAlertException("A new workspace cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return workspaceService
            .save(workspaceDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/workspaces/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /workspaces/:id} : Updates an existing workspace.
     *
     * @param id the id of the workspaceDTO to save.
     * @param workspaceDTO the workspaceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workspaceDTO,
     * or with status {@code 400 (Bad Request)} if the workspaceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workspaceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<WorkspaceDTO>> updateWorkspace(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkspaceDTO workspaceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Workspace : {}, {}", id, workspaceDTO);
        if (workspaceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workspaceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return workspaceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return workspaceService
                    .update(workspaceDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /workspaces/:id} : Partial updates given fields of an existing workspace, field will ignore if it is null
     *
     * @param id the id of the workspaceDTO to save.
     * @param workspaceDTO the workspaceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workspaceDTO,
     * or with status {@code 400 (Bad Request)} if the workspaceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workspaceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workspaceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<WorkspaceDTO>> partialUpdateWorkspace(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkspaceDTO workspaceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Workspace partially : {}, {}", id, workspaceDTO);
        if (workspaceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workspaceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return workspaceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<WorkspaceDTO> result = workspaceService.partialUpdate(workspaceDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /workspaces} : get all the workspaces.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workspaces in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<WorkspaceDTO>>> getAllWorkspaces(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Workspaces");
        return workspaceService
            .countAll()
            .zipWith(workspaceService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity.ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /workspaces/:id} : get the "id" workspace.
     *
     * @param id the id of the workspaceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workspaceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<WorkspaceDTO>> getWorkspace(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Workspace : {}", id);
        Mono<WorkspaceDTO> workspaceDTO = workspaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workspaceDTO);
    }

    /**
     * {@code DELETE  /workspaces/:id} : delete the "id" workspace.
     *
     * @param id the id of the workspaceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteWorkspace(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Workspace : {}", id);
        return workspaceService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
