package edtech.repository;

import edtech.domain.LinkResource;
import edtech.repository.rowmapper.AsciidocSlideRowMapper;
import edtech.repository.rowmapper.LinkResourceRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the LinkResource entity.
 */
@SuppressWarnings("unused")
class LinkResourceRepositoryInternalImpl extends SimpleR2dbcRepository<LinkResource, Long> implements LinkResourceRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final AsciidocSlideRowMapper asciidocslideMapper;
    private final LinkResourceRowMapper linkresourceMapper;

    private static final Table entityTable = Table.aliased("link_resource", EntityManager.ENTITY_ALIAS);
    private static final Table asciidocSlideTable = Table.aliased("asciidoc_slide", "asciidocSlide");

    public LinkResourceRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        AsciidocSlideRowMapper asciidocslideMapper,
        LinkResourceRowMapper linkresourceMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(LinkResource.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.asciidocslideMapper = asciidocslideMapper;
        this.linkresourceMapper = linkresourceMapper;
    }

    @Override
    public Flux<LinkResource> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<LinkResource> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = LinkResourceSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(AsciidocSlideSqlHelper.getColumns(asciidocSlideTable, "asciidocSlide"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(asciidocSlideTable)
            .on(Column.create("asciidoc_slide_id", entityTable))
            .equals(Column.create("id", asciidocSlideTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, LinkResource.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<LinkResource> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<LinkResource> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private LinkResource process(Row row, RowMetadata metadata) {
        LinkResource entity = linkresourceMapper.apply(row, "e");
        entity.setAsciidocSlide(asciidocslideMapper.apply(row, "asciidocSlide"));
        return entity;
    }

    @Override
    public <S extends LinkResource> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
