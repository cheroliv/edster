package edtech.repository;

import edtech.domain.QRCodeResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the QRCodeResource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QRCodeResourceRepository extends ReactiveCrudRepository<QRCodeResource, Long>, QRCodeResourceRepositoryInternal {
    Flux<QRCodeResource> findAllBy(Pageable pageable);

    @Query("SELECT * FROM qr_code_resource entity WHERE entity.asciidoc_slide_id = :id")
    Flux<QRCodeResource> findByAsciidocSlide(Long id);

    @Query("SELECT * FROM qr_code_resource entity WHERE entity.asciidoc_slide_id IS NULL")
    Flux<QRCodeResource> findAllWhereAsciidocSlideIsNull();

    @Override
    <S extends QRCodeResource> Mono<S> save(S entity);

    @Override
    Flux<QRCodeResource> findAll();

    @Override
    Mono<QRCodeResource> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface QRCodeResourceRepositoryInternal {
    <S extends QRCodeResource> Mono<S> save(S entity);

    Flux<QRCodeResource> findAllBy(Pageable pageable);

    Flux<QRCodeResource> findAll();

    Mono<QRCodeResource> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<QRCodeResource> findAllBy(Pageable pageable, Criteria criteria);
}
