package com.arquisocios.apigw.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class HotelSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("cadena", table, columnPrefix + "_cadena"));
        columns.add(Column.aliased("ciudad", table, columnPrefix + "_ciudad"));
        columns.add(Column.aliased("direccion", table, columnPrefix + "_direccion"));

        return columns;
    }
}
