package edtech.web.rest;

import edtech.repository.LinkResourceRepository;
import edtech.service.LinkResourceService;
import edtech.service.dto.LinkResourceDTO;
import edtech.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link edtech.domain.LinkResource}.
 */
@RestController
@RequestMapping("/api/link-resources")
public class LinkResourceResource {

    private static final Logger LOG = LoggerFactory.getLogger(LinkResourceResource.class);

    private static final String ENTITY_NAME = "linkResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LinkResourceService linkResourceService;

    private final LinkResourceRepository linkResourceRepository;

    public LinkResourceResource(LinkResourceService linkResourceService, LinkResourceRepository linkResourceRepository) {
        this.linkResourceService = linkResourceService;
        this.linkResourceRepository = linkResourceRepository;
    }

    /**
     * {@code POST  /link-resources} : Create a new linkResource.
     *
     * @param linkResourceDTO the linkResourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new linkResourceDTO, or with status {@code 400 (Bad Request)} if the linkResource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<LinkResourceDTO>> createLinkResource(@RequestBody LinkResourceDTO linkResourceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save LinkResource : {}", linkResourceDTO);
        if (linkResourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new linkResource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return linkResourceService
            .save(linkResourceDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/link-resources/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /link-resources/:id} : Updates an existing linkResource.
     *
     * @param id the id of the linkResourceDTO to save.
     * @param linkResourceDTO the linkResourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated linkResourceDTO,
     * or with status {@code 400 (Bad Request)} if the linkResourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the linkResourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<LinkResourceDTO>> updateLinkResource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LinkResourceDTO linkResourceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LinkResource : {}, {}", id, linkResourceDTO);
        if (linkResourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, linkResourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return linkResourceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return linkResourceService
                    .update(linkResourceDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /link-resources/:id} : Partial updates given fields of an existing linkResource, field will ignore if it is null
     *
     * @param id the id of the linkResourceDTO to save.
     * @param linkResourceDTO the linkResourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated linkResourceDTO,
     * or with status {@code 400 (Bad Request)} if the linkResourceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the linkResourceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the linkResourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<LinkResourceDTO>> partialUpdateLinkResource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LinkResourceDTO linkResourceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LinkResource partially : {}, {}", id, linkResourceDTO);
        if (linkResourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, linkResourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return linkResourceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<LinkResourceDTO> result = linkResourceService.partialUpdate(linkResourceDTO);

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
     * {@code GET  /link-resources} : get all the linkResources.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of linkResources in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<LinkResourceDTO>>> getAllLinkResources(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of LinkResources");
        return linkResourceService
            .countAll()
            .zipWith(linkResourceService.findAll(pageable).collectList())
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
     * {@code GET  /link-resources/:id} : get the "id" linkResource.
     *
     * @param id the id of the linkResourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the linkResourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<LinkResourceDTO>> getLinkResource(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LinkResource : {}", id);
        Mono<LinkResourceDTO> linkResourceDTO = linkResourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(linkResourceDTO);
    }

    /**
     * {@code DELETE  /link-resources/:id} : delete the "id" linkResource.
     *
     * @param id the id of the linkResourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteLinkResource(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LinkResource : {}", id);
        return linkResourceService
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
