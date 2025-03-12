package edtech.web.rest;

import edtech.repository.PlantUMLDiagramResourceRepository;
import edtech.service.PlantUMLDiagramResourceService;
import edtech.service.dto.PlantUMLDiagramResourceDTO;
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
 * REST controller for managing {@link edtech.domain.PlantUMLDiagramResource}.
 */
@RestController
@RequestMapping("/api/plant-uml-diagram-resources")
public class PlantUMLDiagramResourceResource {

    private static final Logger LOG = LoggerFactory.getLogger(PlantUMLDiagramResourceResource.class);

    private static final String ENTITY_NAME = "plantUMLDiagramResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlantUMLDiagramResourceService plantUMLDiagramResourceService;

    private final PlantUMLDiagramResourceRepository plantUMLDiagramResourceRepository;

    public PlantUMLDiagramResourceResource(
        PlantUMLDiagramResourceService plantUMLDiagramResourceService,
        PlantUMLDiagramResourceRepository plantUMLDiagramResourceRepository
    ) {
        this.plantUMLDiagramResourceService = plantUMLDiagramResourceService;
        this.plantUMLDiagramResourceRepository = plantUMLDiagramResourceRepository;
    }

    /**
     * {@code POST  /plant-uml-diagram-resources} : Create a new plantUMLDiagramResource.
     *
     * @param plantUMLDiagramResourceDTO the plantUMLDiagramResourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plantUMLDiagramResourceDTO, or with status {@code 400 (Bad Request)} if the plantUMLDiagramResource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PlantUMLDiagramResourceDTO>> createPlantUMLDiagramResource(
        @RequestBody PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PlantUMLDiagramResource : {}", plantUMLDiagramResourceDTO);
        if (plantUMLDiagramResourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new plantUMLDiagramResource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return plantUMLDiagramResourceService
            .save(plantUMLDiagramResourceDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/plant-uml-diagram-resources/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /plant-uml-diagram-resources/:id} : Updates an existing plantUMLDiagramResource.
     *
     * @param id the id of the plantUMLDiagramResourceDTO to save.
     * @param plantUMLDiagramResourceDTO the plantUMLDiagramResourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plantUMLDiagramResourceDTO,
     * or with status {@code 400 (Bad Request)} if the plantUMLDiagramResourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plantUMLDiagramResourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PlantUMLDiagramResourceDTO>> updatePlantUMLDiagramResource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PlantUMLDiagramResource : {}, {}", id, plantUMLDiagramResourceDTO);
        if (plantUMLDiagramResourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plantUMLDiagramResourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return plantUMLDiagramResourceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return plantUMLDiagramResourceService
                    .update(plantUMLDiagramResourceDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /plant-uml-diagram-resources/:id} : Partial updates given fields of an existing plantUMLDiagramResource, field will ignore if it is null
     *
     * @param id the id of the plantUMLDiagramResourceDTO to save.
     * @param plantUMLDiagramResourceDTO the plantUMLDiagramResourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plantUMLDiagramResourceDTO,
     * or with status {@code 400 (Bad Request)} if the plantUMLDiagramResourceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the plantUMLDiagramResourceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the plantUMLDiagramResourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PlantUMLDiagramResourceDTO>> partialUpdatePlantUMLDiagramResource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlantUMLDiagramResourceDTO plantUMLDiagramResourceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PlantUMLDiagramResource partially : {}, {}", id, plantUMLDiagramResourceDTO);
        if (plantUMLDiagramResourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plantUMLDiagramResourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return plantUMLDiagramResourceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PlantUMLDiagramResourceDTO> result = plantUMLDiagramResourceService.partialUpdate(plantUMLDiagramResourceDTO);

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
     * {@code GET  /plant-uml-diagram-resources} : get all the plantUMLDiagramResources.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plantUMLDiagramResources in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PlantUMLDiagramResourceDTO>>> getAllPlantUMLDiagramResources(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of PlantUMLDiagramResources");
        return plantUMLDiagramResourceService
            .countAll()
            .zipWith(plantUMLDiagramResourceService.findAll(pageable).collectList())
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
     * {@code GET  /plant-uml-diagram-resources/:id} : get the "id" plantUMLDiagramResource.
     *
     * @param id the id of the plantUMLDiagramResourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plantUMLDiagramResourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PlantUMLDiagramResourceDTO>> getPlantUMLDiagramResource(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PlantUMLDiagramResource : {}", id);
        Mono<PlantUMLDiagramResourceDTO> plantUMLDiagramResourceDTO = plantUMLDiagramResourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(plantUMLDiagramResourceDTO);
    }

    /**
     * {@code DELETE  /plant-uml-diagram-resources/:id} : delete the "id" plantUMLDiagramResource.
     *
     * @param id the id of the plantUMLDiagramResourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePlantUMLDiagramResource(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PlantUMLDiagramResource : {}", id);
        return plantUMLDiagramResourceService
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
