package edtech.repository;

import edtech.domain.AsciidocSlide;
import edtech.repository.rowmapper.AsciidocSlideRowMapper;
import edtech.repository.rowmapper.PresentationRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the AsciidocSlide entity.
 */
@SuppressWarnings("unused")
class AsciidocSlideRepositoryInternalImpl extends SimpleR2dbcRepository<AsciidocSlide, Long> implements AsciidocSlideRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PresentationRowMapper presentationMapper;
    private final AsciidocSlideRowMapper asciidocslideMapper;

    private static final Table entityTable = Table.aliased("asciidoc_slide", EntityManager.ENTITY_ALIAS);
    private static final Table presentationTable = Table.aliased("presentation", "presentation");

    public AsciidocSlideRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PresentationRowMapper presentationMapper,
        AsciidocSlideRowMapper asciidocslideMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(AsciidocSlide.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.presentationMapper = presentationMapper;
        this.asciidocslideMapper = asciidocslideMapper;
    }

    @Override
    public Flux<AsciidocSlide> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<AsciidocSlide> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = AsciidocSlideSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PresentationSqlHelper.getColumns(presentationTable, "presentation"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(presentationTable)
            .on(Column.create("presentation_id", entityTable))
            .equals(Column.create("id", presentationTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, AsciidocSlide.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<AsciidocSlide> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<AsciidocSlide> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private AsciidocSlide process(Row row, RowMetadata metadata) {
        AsciidocSlide entity = asciidocslideMapper.apply(row, "e");
        entity.setPresentation(presentationMapper.apply(row, "presentation"));
        return entity;
    }

    @Override
    public <S extends AsciidocSlide> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
