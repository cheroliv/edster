package edtech.repository;

import edtech.domain.Workspace;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Workspace entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkspaceRepository extends ReactiveCrudRepository<Workspace, Long>, WorkspaceRepositoryInternal {
    Flux<Workspace> findAllBy(Pageable pageable);

    @Override
    Mono<Workspace> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Workspace> findAllWithEagerRelationships();

    @Override
    Flux<Workspace> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM workspace entity WHERE entity.user_id = :id")
    Flux<Workspace> findByUser(Long id);

    @Query("SELECT * FROM workspace entity WHERE entity.user_id IS NULL")
    Flux<Workspace> findAllWhereUserIsNull();

    @Override
    <S extends Workspace> Mono<S> save(S entity);

    @Override
    Flux<Workspace> findAll();

    @Override
    Mono<Workspace> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface WorkspaceRepositoryInternal {
    <S extends Workspace> Mono<S> save(S entity);

    Flux<Workspace> findAllBy(Pageable pageable);

    Flux<Workspace> findAll();

    Mono<Workspace> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Workspace> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Workspace> findOneWithEagerRelationships(Long id);

    Flux<Workspace> findAllWithEagerRelationships();

    Flux<Workspace> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
