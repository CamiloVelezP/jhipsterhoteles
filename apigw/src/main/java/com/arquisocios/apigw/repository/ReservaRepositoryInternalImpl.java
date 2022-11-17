package com.arquisocios.apigw.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.arquisocios.apigw.domain.Reserva;
import com.arquisocios.apigw.repository.rowmapper.HabitacionRowMapper;
import com.arquisocios.apigw.repository.rowmapper.ReservaRowMapper;
import com.arquisocios.apigw.repository.rowmapper.UsuarioRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
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
 * Spring Data R2DBC custom repository implementation for the Reserva entity.
 */
@SuppressWarnings("unused")
class ReservaRepositoryInternalImpl extends SimpleR2dbcRepository<Reserva, Long> implements ReservaRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final HabitacionRowMapper habitacionMapper;
    private final UsuarioRowMapper usuarioMapper;
    private final ReservaRowMapper reservaMapper;

    private static final Table entityTable = Table.aliased("reserva", EntityManager.ENTITY_ALIAS);
    private static final Table idHabitacionTable = Table.aliased("habitacion", "idHabitacion");
    private static final Table nroDocTable = Table.aliased("usuario", "nroDoc");

    public ReservaRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        HabitacionRowMapper habitacionMapper,
        UsuarioRowMapper usuarioMapper,
        ReservaRowMapper reservaMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Reserva.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.habitacionMapper = habitacionMapper;
        this.usuarioMapper = usuarioMapper;
        this.reservaMapper = reservaMapper;
    }

    @Override
    public Flux<Reserva> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Reserva> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ReservaSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(HabitacionSqlHelper.getColumns(idHabitacionTable, "idHabitacion"));
        columns.addAll(UsuarioSqlHelper.getColumns(nroDocTable, "nroDoc"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(idHabitacionTable)
            .on(Column.create("id_habitacion_id", entityTable))
            .equals(Column.create("id", idHabitacionTable))
            .leftOuterJoin(nroDocTable)
            .on(Column.create("nro_doc_id", entityTable))
            .equals(Column.create("id", nroDocTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Reserva.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Reserva> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Reserva> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Reserva process(Row row, RowMetadata metadata) {
        Reserva entity = reservaMapper.apply(row, "e");
        entity.setIdHabitacion(habitacionMapper.apply(row, "idHabitacion"));
        entity.setNroDoc(usuarioMapper.apply(row, "nroDoc"));
        return entity;
    }

    @Override
    public <S extends Reserva> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
