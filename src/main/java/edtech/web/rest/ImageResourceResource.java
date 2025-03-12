package edtech.web.rest;

import edtech.repository.ImageResourceRepository;
import edtech.service.ImageResourceService;
import edtech.service.dto.ImageResourceDTO;
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
 * REST controller for managing {@link edtech.domain.ImageResource}.
 */
@RestController
@RequestMapping("/api/image-resources")
public class ImageResourceResource {

    private static final Logger LOG = LoggerFactory.getLogger(ImageResourceResource.class);

    private static final String ENTITY_NAME = "imageResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageResourceService imageResourceService;

    private final ImageResourceRepository imageResourceRepository;

    public ImageResourceResource(ImageResourceService imageResourceService, ImageResourceRepository imageResourceRepository) {
        this.imageResourceService = imageResourceService;
        this.imageResourceRepository = imageResourceRepository;
    }

    /**
     * {@code POST  /image-resources} : Create a new imageResource.
     *
     * @param imageResourceDTO the imageResourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imageResourceDTO, or with status {@code 400 (Bad Request)} if the imageResource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ImageResourceDTO>> createImageResource(@RequestBody ImageResourceDTO imageResourceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ImageResource : {}", imageResourceDTO);
        if (imageResourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new imageResource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return imageResourceService
            .save(imageResourceDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/image-resources/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /image-resources/:id} : Updates an existing imageResource.
     *
     * @param id the id of the imageResourceDTO to save.
     * @param imageResourceDTO the imageResourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageResourceDTO,
     * or with status {@code 400 (Bad Request)} if the imageResourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imageResourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ImageResourceDTO>> updateImageResource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImageResourceDTO imageResourceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ImageResource : {}, {}", id, imageResourceDTO);
        if (imageResourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageResourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return imageResourceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return imageResourceService
                    .update(imageResourceDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /image-resources/:id} : Partial updates given fields of an existing imageResource, field will ignore if it is null
     *
     * @param id the id of the imageResourceDTO to save.
     * @param imageResourceDTO the imageResourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageResourceDTO,
     * or with status {@code 400 (Bad Request)} if the imageResourceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the imageResourceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the imageResourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ImageResourceDTO>> partialUpdateImageResource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImageResourceDTO imageResourceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ImageResource partially : {}, {}", id, imageResourceDTO);
        if (imageResourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageResourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return imageResourceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ImageResourceDTO> result = imageResourceService.partialUpdate(imageResourceDTO);

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
     * {@code GET  /image-resources} : get all the imageResources.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imageResources in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ImageResourceDTO>>> getAllImageResources(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of ImageResources");
        return imageResourceService
            .countAll()
            .zipWith(imageResourceService.findAll(pageable).collectList())
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
     * {@code GET  /image-resources/:id} : get the "id" imageResource.
     *
     * @param id the id of the imageResourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imageResourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ImageResourceDTO>> getImageResource(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ImageResource : {}", id);
        Mono<ImageResourceDTO> imageResourceDTO = imageResourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imageResourceDTO);
    }

    /**
     * {@code DELETE  /image-resources/:id} : delete the "id" imageResource.
     *
     * @param id the id of the imageResourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteImageResource(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ImageResource : {}", id);
        return imageResourceService
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
