package edtech.repository;

import edtech.domain.AsciidocSlide;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the AsciidocSlide entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AsciidocSlideRepository extends ReactiveCrudRepository<AsciidocSlide, Long>, AsciidocSlideRepositoryInternal {
    Flux<AsciidocSlide> findAllBy(Pageable pageable);

    @Query("SELECT * FROM asciidoc_slide entity WHERE entity.presentation_id = :id")
    Flux<AsciidocSlide> findByPresentation(Long id);

    @Query("SELECT * FROM asciidoc_slide entity WHERE entity.presentation_id IS NULL")
    Flux<AsciidocSlide> findAllWherePresentationIsNull();

    @Override
    <S extends AsciidocSlide> Mono<S> save(S entity);

    @Override
    Flux<AsciidocSlide> findAll();

    @Override
    Mono<AsciidocSlide> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AsciidocSlideRepositoryInternal {
    <S extends AsciidocSlide> Mono<S> save(S entity);

    Flux<AsciidocSlide> findAllBy(Pageable pageable);

    Flux<AsciidocSlide> findAll();

    Mono<AsciidocSlide> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AsciidocSlide> findAllBy(Pageable pageable, Criteria criteria);
}
