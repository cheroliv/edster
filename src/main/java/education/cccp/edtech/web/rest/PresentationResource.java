package education.cccp.edtech.web.rest;

import education.cccp.edtech.domain.criteria.PresentationCriteria;
import education.cccp.edtech.repository.PresentationRepository;
import education.cccp.edtech.service.PresentationService;
import education.cccp.edtech.service.dto.PresentationDTO;
import education.cccp.edtech.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link education.cccp.edtech.domain.Presentation}.
 */
@RestController
@RequestMapping("/api/presentations")
public class PresentationResource {

    private static final Logger LOG = LoggerFactory.getLogger(PresentationResource.class);

    private static final String ENTITY_NAME = "presentation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PresentationService presentationService;

    private final PresentationRepository presentationRepository;

    public PresentationResource(PresentationService presentationService, PresentationRepository presentationRepository) {
        this.presentationService = presentationService;
        this.presentationRepository = presentationRepository;
    }

    /**
     * {@code POST  /presentations} : Create a new presentation.
     *
     * @param presentationDTO the presentationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new presentationDTO, or with status {@code 400 (Bad Request)} if the presentation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PresentationDTO>> createPresentation(@Valid @RequestBody PresentationDTO presentationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Presentation : {}", presentationDTO);
        if (presentationDTO.getId() != null) {
            throw new BadRequestAlertException("A new presentation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return presentationService
            .save(presentationDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/presentations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /presentations/:id} : Updates an existing presentation.
     *
     * @param id the id of the presentationDTO to save.
     * @param presentationDTO the presentationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated presentationDTO,
     * or with status {@code 400 (Bad Request)} if the presentationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the presentationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PresentationDTO>> updatePresentation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PresentationDTO presentationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Presentation : {}, {}", id, presentationDTO);
        if (presentationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, presentationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return presentationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return presentationService
                    .update(presentationDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /presentations/:id} : Partial updates given fields of an existing presentation, field will ignore if it is null
     *
     * @param id the id of the presentationDTO to save.
     * @param presentationDTO the presentationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated presentationDTO,
     * or with status {@code 400 (Bad Request)} if the presentationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the presentationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the presentationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PresentationDTO>> partialUpdatePresentation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PresentationDTO presentationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Presentation partially : {}, {}", id, presentationDTO);
        if (presentationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, presentationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return presentationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PresentationDTO> result = presentationService.partialUpdate(presentationDTO);

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
     * {@code GET  /presentations} : get all the presentations.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of presentations in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PresentationDTO>>> getAllPresentations(
        PresentationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get Presentations by criteria: {}", criteria);
        return presentationService
            .countByCriteria(criteria)
            .zipWith(presentationService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /presentations/count} : count all the presentations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countPresentations(PresentationCriteria criteria) {
        LOG.debug("REST request to count Presentations by criteria: {}", criteria);
        return presentationService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /presentations/:id} : get the "id" presentation.
     *
     * @param id the id of the presentationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the presentationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PresentationDTO>> getPresentation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Presentation : {}", id);
        Mono<PresentationDTO> presentationDTO = presentationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(presentationDTO);
    }

    /**
     * {@code DELETE  /presentations/:id} : delete the "id" presentation.
     *
     * @param id the id of the presentationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePresentation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Presentation : {}", id);
        return presentationService
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
