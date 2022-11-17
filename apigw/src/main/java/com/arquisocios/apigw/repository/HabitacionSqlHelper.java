package com.arquisocios.apigw.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class HabitacionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("ubicacion", table, columnPrefix + "_ubicacion"));
        columns.add(Column.aliased("capacidad", table, columnPrefix + "_capacidad"));
        columns.add(Column.aliased("precio", table, columnPrefix + "_precio"));
        columns.add(Column.aliased("disponible", table, columnPrefix + "_disponible"));

        columns.add(Column.aliased("id_hotel_id", table, columnPrefix + "_id_hotel_id"));
        return columns;
    }
}
