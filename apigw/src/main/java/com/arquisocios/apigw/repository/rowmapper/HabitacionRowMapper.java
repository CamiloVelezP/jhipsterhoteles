package com.arquisocios.apigw.repository.rowmapper;

import com.arquisocios.apigw.domain.Habitacion;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Habitacion}, with proper type conversions.
 */
@Service
public class HabitacionRowMapper implements BiFunction<Row, String, Habitacion> {

    private final ColumnConverter converter;

    public HabitacionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Habitacion} stored in the database.
     */
    @Override
    public Habitacion apply(Row row, String prefix) {
        Habitacion entity = new Habitacion();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUbicacion(converter.fromRow(row, prefix + "_ubicacion", String.class));
        entity.setCapacidad(converter.fromRow(row, prefix + "_capacidad", Integer.class));
        entity.setPrecio(converter.fromRow(row, prefix + "_precio", Float.class));
        entity.setDisponible(converter.fromRow(row, prefix + "_disponible", Boolean.class));
        entity.setIdHotelId(converter.fromRow(row, prefix + "_id_hotel_id", Long.class));
        return entity;
    }
}
