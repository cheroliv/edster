package education.cccp.edtech.repository;

import education.cccp.edtech.domain.Presentation;
import education.cccp.edtech.domain.criteria.PresentationCriteria;
import education.cccp.edtech.repository.rowmapper.ColumnConverter;
import education.cccp.edtech.repository.rowmapper.PresentationRowMapper;
import education.cccp.edtech.repository.rowmapper.UserRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
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
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the Presentation entity.
 */
@SuppressWarnings("unused")
class PresentationRepositoryInternalImpl extends SimpleR2dbcRepository<Presentation, Long> implements PresentationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserRowMapper userMapper;
    private final PresentationRowMapper presentationMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("presentation", EntityManager.ENTITY_ALIAS);
    private static final Table userTable = Table.aliased("jhi_user", "e_user");

    public PresentationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserRowMapper userMapper,
        PresentationRowMapper presentationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Presentation.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.presentationMapper = presentationMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Presentation> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Presentation> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PresentationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserSqlHelper.getColumns(userTable, "user"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable));
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

    @Override
    public Mono<Presentation> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Presentation> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Presentation> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Presentation process(Row row, RowMetadata metadata) {
        Presentation entity = presentationMapper.apply(row, "e");
        entity.setUser(userMapper.apply(row, "user"));
        return entity;
    }

    @Override
    public <S extends Presentation> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<Presentation> findByCriteria(PresentationCriteria presentationCriteria, Pageable page) {
        return createQuery(page, buildConditions(presentationCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(PresentationCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(PresentationCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getPlan() != null) {
                builder.buildFilterConditionForField(criteria.getPlan(), entityTable.column("plan"));
            }
            if (criteria.getUri() != null) {
                builder.buildFilterConditionForField(criteria.getUri(), entityTable.column("uri"));
            }
            if (criteria.getUserId() != null) {
                builder.buildFilterConditionForField(criteria.getUserId(), userTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
