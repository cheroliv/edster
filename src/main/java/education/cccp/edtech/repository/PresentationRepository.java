package education.cccp.edtech.repository;

import education.cccp.edtech.domain.Presentation;
import education.cccp.edtech.domain.criteria.PresentationCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Presentation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PresentationRepository extends ReactiveCrudRepository<Presentation, Long>, PresentationRepositoryInternal {
    Flux<Presentation> findAllBy(Pageable pageable);

    @Override
    Mono<Presentation> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Presentation> findAllWithEagerRelationships();

    @Override
    Flux<Presentation> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM presentation entity WHERE entity.user_id = :id")
    Flux<Presentation> findByUser(Long id);

    @Query("SELECT * FROM presentation entity WHERE entity.user_id IS NULL")
    Flux<Presentation> findAllWhereUserIsNull();

    @Override
    <S extends Presentation> Mono<S> save(S entity);

    @Override
    Flux<Presentation> findAll();

    @Override
    Mono<Presentation> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PresentationRepositoryInternal {
    <S extends Presentation> Mono<S> save(S entity);

    Flux<Presentation> findAllBy(Pageable pageable);

    Flux<Presentation> findAll();

    Mono<Presentation> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Presentation> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Presentation> findByCriteria(PresentationCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(PresentationCriteria criteria);

    Mono<Presentation> findOneWithEagerRelationships(Long id);

    Flux<Presentation> findAllWithEagerRelationships();

    Flux<Presentation> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
