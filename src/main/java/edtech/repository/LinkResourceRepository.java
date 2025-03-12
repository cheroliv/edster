package edtech.repository;

import edtech.domain.LinkResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the LinkResource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LinkResourceRepository extends ReactiveCrudRepository<LinkResource, Long>, LinkResourceRepositoryInternal {
    Flux<LinkResource> findAllBy(Pageable pageable);

    @Query("SELECT * FROM link_resource entity WHERE entity.asciidoc_slide_id = :id")
    Flux<LinkResource> findByAsciidocSlide(Long id);

    @Query("SELECT * FROM link_resource entity WHERE entity.asciidoc_slide_id IS NULL")
    Flux<LinkResource> findAllWhereAsciidocSlideIsNull();

    @Override
    <S extends LinkResource> Mono<S> save(S entity);

    @Override
    Flux<LinkResource> findAll();

    @Override
    Mono<LinkResource> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface LinkResourceRepositoryInternal {
    <S extends LinkResource> Mono<S> save(S entity);

    Flux<LinkResource> findAllBy(Pageable pageable);

    Flux<LinkResource> findAll();

    Mono<LinkResource> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<LinkResource> findAllBy(Pageable pageable, Criteria criteria);
}
