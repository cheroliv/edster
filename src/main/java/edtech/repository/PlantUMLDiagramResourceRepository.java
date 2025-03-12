package edtech.repository;

import edtech.domain.PlantUMLDiagramResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PlantUMLDiagramResource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlantUMLDiagramResourceRepository
    extends ReactiveCrudRepository<PlantUMLDiagramResource, Long>, PlantUMLDiagramResourceRepositoryInternal {
    Flux<PlantUMLDiagramResource> findAllBy(Pageable pageable);

    @Query("SELECT * FROM plant_uml_diagram_resource entity WHERE entity.asciidoc_slide_id = :id")
    Flux<PlantUMLDiagramResource> findByAsciidocSlide(Long id);

    @Query("SELECT * FROM plant_uml_diagram_resource entity WHERE entity.asciidoc_slide_id IS NULL")
    Flux<PlantUMLDiagramResource> findAllWhereAsciidocSlideIsNull();

    @Override
    <S extends PlantUMLDiagramResource> Mono<S> save(S entity);

    @Override
    Flux<PlantUMLDiagramResource> findAll();

    @Override
    Mono<PlantUMLDiagramResource> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PlantUMLDiagramResourceRepositoryInternal {
    <S extends PlantUMLDiagramResource> Mono<S> save(S entity);

    Flux<PlantUMLDiagramResource> findAllBy(Pageable pageable);

    Flux<PlantUMLDiagramResource> findAll();

    Mono<PlantUMLDiagramResource> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PlantUMLDiagramResource> findAllBy(Pageable pageable, Criteria criteria);
}
