package com.arquisocios.apigw.repository.rowmapper;

import com.arquisocios.apigw.domain.Hotel;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Hotel}, with proper type conversions.
 */
@Service
public class HotelRowMapper implements BiFunction<Row, String, Hotel> {

    private final ColumnConverter converter;

    public HotelRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Hotel} stored in the database.
     */
    @Override
    public Hotel apply(Row row, String prefix) {
        Hotel entity = new Hotel();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setCadena(converter.fromRow(row, prefix + "_cadena", String.class));
        entity.setCiudad(converter.fromRow(row, prefix + "_ciudad", String.class));
        entity.setDireccion(converter.fromRow(row, prefix + "_direccion", String.class));
        return entity;
    }
}
