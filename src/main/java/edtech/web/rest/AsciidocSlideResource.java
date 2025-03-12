package edtech.web.rest;

import edtech.repository.AsciidocSlideRepository;
import edtech.service.AsciidocSlideService;
import edtech.service.dto.AsciidocSlideDTO;
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
 * REST controller for managing {@link edtech.domain.AsciidocSlide}.
 */
@RestController
@RequestMapping("/api/asciidoc-slides")
public class AsciidocSlideResource {

    private static final Logger LOG = LoggerFactory.getLogger(AsciidocSlideResource.class);

    private static final String ENTITY_NAME = "asciidocSlide";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AsciidocSlideService asciidocSlideService;

    private final AsciidocSlideRepository asciidocSlideRepository;

    public AsciidocSlideResource(AsciidocSlideService asciidocSlideService, AsciidocSlideRepository asciidocSlideRepository) {
        this.asciidocSlideService = asciidocSlideService;
        this.asciidocSlideRepository = asciidocSlideRepository;
    }

    /**
     * {@code POST  /asciidoc-slides} : Create a new asciidocSlide.
     *
     * @param asciidocSlideDTO the asciidocSlideDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new asciidocSlideDTO, or with status {@code 400 (Bad Request)} if the asciidocSlide has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AsciidocSlideDTO>> createAsciidocSlide(@Valid @RequestBody AsciidocSlideDTO asciidocSlideDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AsciidocSlide : {}", asciidocSlideDTO);
        if (asciidocSlideDTO.getId() != null) {
            throw new BadRequestAlertException("A new asciidocSlide cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return asciidocSlideService
            .save(asciidocSlideDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/asciidoc-slides/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /asciidoc-slides/:id} : Updates an existing asciidocSlide.
     *
     * @param id the id of the asciidocSlideDTO to save.
     * @param asciidocSlideDTO the asciidocSlideDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated asciidocSlideDTO,
     * or with status {@code 400 (Bad Request)} if the asciidocSlideDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the asciidocSlideDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AsciidocSlideDTO>> updateAsciidocSlide(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AsciidocSlideDTO asciidocSlideDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AsciidocSlide : {}, {}", id, asciidocSlideDTO);
        if (asciidocSlideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, asciidocSlideDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return asciidocSlideRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return asciidocSlideService
                    .update(asciidocSlideDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /asciidoc-slides/:id} : Partial updates given fields of an existing asciidocSlide, field will ignore if it is null
     *
     * @param id the id of the asciidocSlideDTO to save.
     * @param asciidocSlideDTO the asciidocSlideDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated asciidocSlideDTO,
     * or with status {@code 400 (Bad Request)} if the asciidocSlideDTO is not valid,
     * or with status {@code 404 (Not Found)} if the asciidocSlideDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the asciidocSlideDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AsciidocSlideDTO>> partialUpdateAsciidocSlide(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AsciidocSlideDTO asciidocSlideDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AsciidocSlide partially : {}, {}", id, asciidocSlideDTO);
        if (asciidocSlideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, asciidocSlideDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return asciidocSlideRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AsciidocSlideDTO> result = asciidocSlideService.partialUpdate(asciidocSlideDTO);

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
     * {@code GET  /asciidoc-slides} : get all the asciidocSlides.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of asciidocSlides in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<AsciidocSlideDTO>>> getAllAsciidocSlides(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of AsciidocSlides");
        return asciidocSlideService
            .countAll()
            .zipWith(asciidocSlideService.findAll(pageable).collectList())
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
     * {@code GET  /asciidoc-slides/:id} : get the "id" asciidocSlide.
     *
     * @param id the id of the asciidocSlideDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the asciidocSlideDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AsciidocSlideDTO>> getAsciidocSlide(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AsciidocSlide : {}", id);
        Mono<AsciidocSlideDTO> asciidocSlideDTO = asciidocSlideService.findOne(id);
        return ResponseUtil.wrapOrNotFound(asciidocSlideDTO);
    }

    /**
     * {@code DELETE  /asciidoc-slides/:id} : delete the "id" asciidocSlide.
     *
     * @param id the id of the asciidocSlideDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAsciidocSlide(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AsciidocSlide : {}", id);
        return asciidocSlideService
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
