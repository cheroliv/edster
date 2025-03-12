package edtech.repository;

import edtech.domain.Presentation;
import edtech.repository.rowmapper.PresentationRowMapper;
import edtech.repository.rowmapper.WorkspaceRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Presentation entity.
 */
@SuppressWarnings("unused")
class PresentationRepositoryInternalImpl extends SimpleR2dbcRepository<Presentation, Long> implements PresentationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final WorkspaceRowMapper workspaceMapper;
    private final PresentationRowMapper presentationMapper;

    private static final Table entityTable = Table.aliased("presentation", EntityManager.ENTITY_ALIAS);
    private static final Table workspaceTable = Table.aliased("workspace", "workspace");

    public PresentationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        WorkspaceRowMapper workspaceMapper,
        PresentationRowMapper presentationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Presentation.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.workspaceMapper = workspaceMapper;
        this.presentationMapper = presentationMapper;
    }

    @Override
    public Flux<Presentation> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Presentation> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PresentationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(WorkspaceSqlHelper.getColumns(workspaceTable, "workspace"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(workspaceTable)
            .on(Column.create("workspace_id", entityTable))
            .equals(Column.create("id", workspaceTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Presentation.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Presentation> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Presentation> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Presentation process(Row row, RowMetadata metadata) {
        Presentation entity = presentationMapper.apply(row, "e");
        entity.setWorkspace(workspaceMapper.apply(row, "workspace"));
        return entity;
    }

    @Override
    public <S extends Presentation> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
