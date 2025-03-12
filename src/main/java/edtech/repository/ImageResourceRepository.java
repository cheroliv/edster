package edtech.repository;

import edtech.domain.ImageResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ImageResource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageResourceRepository extends ReactiveCrudRepository<ImageResource, Long>, ImageResourceRepositoryInternal {
    Flux<ImageResource> findAllBy(Pageable pageable);

    @Query("SELECT * FROM image_resource entity WHERE entity.asciidoc_slide_id = :id")
    Flux<ImageResource> findByAsciidocSlide(Long id);

    @Query("SELECT * FROM image_resource entity WHERE entity.asciidoc_slide_id IS NULL")
    Flux<ImageResource> findAllWhereAsciidocSlideIsNull();

    @Override
    <S extends ImageResource> Mono<S> save(S entity);

    @Override
    Flux<ImageResource> findAll();

    @Override
    Mono<ImageResource> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ImageResourceRepositoryInternal {
    <S extends ImageResource> Mono<S> save(S entity);

    Flux<ImageResource> findAllBy(Pageable pageable);

    Flux<ImageResource> findAll();

    Mono<ImageResource> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ImageResource> findAllBy(Pageable pageable, Criteria criteria);
}
