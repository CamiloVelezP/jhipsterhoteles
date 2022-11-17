package com.arquisocios.apigw.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.arquisocios.apigw.domain.Habitacion;
import com.arquisocios.apigw.repository.rowmapper.HabitacionRowMapper;
import com.arquisocios.apigw.repository.rowmapper.HotelRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
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
 * Spring Data R2DBC custom repository implementation for the Habitacion entity.
 */
@SuppressWarnings("unused")
class HabitacionRepositoryInternalImpl extends SimpleR2dbcRepository<Habitacion, Long> implements HabitacionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final HotelRowMapper hotelMapper;
    private final HabitacionRowMapper habitacionMapper;

    private static final Table entityTable = Table.aliased("habitacion", EntityManager.ENTITY_ALIAS);
    private static final Table idHotelTable = Table.aliased("hotel", "idHotel");

    public HabitacionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        HotelRowMapper hotelMapper,
        HabitacionRowMapper habitacionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Habitacion.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.hotelMapper = hotelMapper;
        this.habitacionMapper = habitacionMapper;
    }

    @Override
    public Flux<Habitacion> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Habitacion> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = HabitacionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(HotelSqlHelper.getColumns(idHotelTable, "idHotel"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(idHotelTable)
            .on(Column.create("id_hotel_id", entityTable))
            .equals(Column.create("id", idHotelTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Habitacion.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Habitacion> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Habitacion> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Habitacion process(Row row, RowMetadata metadata) {
        Habitacion entity = habitacionMapper.apply(row, "e");
        entity.setIdHotel(hotelMapper.apply(row, "idHotel"));
        return entity;
    }

    @Override
    public <S extends Habitacion> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
