package com.arquisocios.apigw.repository.rowmapper;

import com.arquisocios.apigw.domain.Reserva;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Reserva}, with proper type conversions.
 */
@Service
public class ReservaRowMapper implements BiFunction<Row, String, Reserva> {

    private final ColumnConverter converter;

    public ReservaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Reserva} stored in the database.
     */
    @Override
    public Reserva apply(Row row, String prefix) {
        Reserva entity = new Reserva();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFechaInicio(converter.fromRow(row, prefix + "_fecha_inicio", Instant.class));
        entity.setFechaFin(converter.fromRow(row, prefix + "_fecha_fin", Instant.class));
        entity.setIdHabitacionId(converter.fromRow(row, prefix + "_id_habitacion_id", Long.class));
        entity.setNroDocId(converter.fromRow(row, prefix + "_nro_doc_id", Long.class));
        return entity;
    }
}
