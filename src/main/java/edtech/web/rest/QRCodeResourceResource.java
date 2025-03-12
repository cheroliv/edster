package edtech.web.rest;

import edtech.repository.QRCodeResourceRepository;
import edtech.service.QRCodeResourceService;
import edtech.service.dto.QRCodeResourceDTO;
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
 * REST controller for managing {@link edtech.domain.QRCodeResource}.
 */
@RestController
@RequestMapping("/api/qr-code-resources")
public class QRCodeResourceResource {

    private static final Logger LOG = LoggerFactory.getLogger(QRCodeResourceResource.class);

    private static final String ENTITY_NAME = "qRCodeResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QRCodeResourceService qRCodeResourceService;

    private final QRCodeResourceRepository qRCodeResourceRepository;

    public QRCodeResourceResource(QRCodeResourceService qRCodeResourceService, QRCodeResourceRepository qRCodeResourceRepository) {
        this.qRCodeResourceService = qRCodeResourceService;
        this.qRCodeResourceRepository = qRCodeResourceRepository;
    }

    /**
     * {@code POST  /qr-code-resources} : Create a new qRCodeResource.
     *
     * @param qRCodeResourceDTO the qRCodeResourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new qRCodeResourceDTO, or with status {@code 400 (Bad Request)} if the qRCodeResource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<QRCodeResourceDTO>> createQRCodeResource(@RequestBody QRCodeResourceDTO qRCodeResourceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save QRCodeResource : {}", qRCodeResourceDTO);
        if (qRCodeResourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new qRCodeResource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return qRCodeResourceService
            .save(qRCodeResourceDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/qr-code-resources/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /qr-code-resources/:id} : Updates an existing qRCodeResource.
     *
     * @param id the id of the qRCodeResourceDTO to save.
     * @param qRCodeResourceDTO the qRCodeResourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qRCodeResourceDTO,
     * or with status {@code 400 (Bad Request)} if the qRCodeResourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the qRCodeResourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<QRCodeResourceDTO>> updateQRCodeResource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QRCodeResourceDTO qRCodeResourceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update QRCodeResource : {}, {}", id, qRCodeResourceDTO);
        if (qRCodeResourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, qRCodeResourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return qRCodeResourceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return qRCodeResourceService
                    .update(qRCodeResourceDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /qr-code-resources/:id} : Partial updates given fields of an existing qRCodeResource, field will ignore if it is null
     *
     * @param id the id of the qRCodeResourceDTO to save.
     * @param qRCodeResourceDTO the qRCodeResourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qRCodeResourceDTO,
     * or with status {@code 400 (Bad Request)} if the qRCodeResourceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the qRCodeResourceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the qRCodeResourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<QRCodeResourceDTO>> partialUpdateQRCodeResource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QRCodeResourceDTO qRCodeResourceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update QRCodeResource partially : {}, {}", id, qRCodeResourceDTO);
        if (qRCodeResourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, qRCodeResourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return qRCodeResourceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<QRCodeResourceDTO> result = qRCodeResourceService.partialUpdate(qRCodeResourceDTO);

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
     * {@code GET  /qr-code-resources} : get all the qRCodeResources.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of qRCodeResources in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<QRCodeResourceDTO>>> getAllQRCodeResources(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of QRCodeResources");
        return qRCodeResourceService
            .countAll()
            .zipWith(qRCodeResourceService.findAll(pageable).collectList())
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
     * {@code GET  /qr-code-resources/:id} : get the "id" qRCodeResource.
     *
     * @param id the id of the qRCodeResourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the qRCodeResourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<QRCodeResourceDTO>> getQRCodeResource(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QRCodeResource : {}", id);
        Mono<QRCodeResourceDTO> qRCodeResourceDTO = qRCodeResourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(qRCodeResourceDTO);
    }

    /**
     * {@code DELETE  /qr-code-resources/:id} : delete the "id" qRCodeResource.
     *
     * @param id the id of the qRCodeResourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteQRCodeResource(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QRCodeResource : {}", id);
        return qRCodeResourceService
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
