package com.arquisocios.apigw.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ReservaSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("fecha_inicio", table, columnPrefix + "_fecha_inicio"));
        columns.add(Column.aliased("fecha_fin", table, columnPrefix + "_fecha_fin"));

        columns.add(Column.aliased("id_habitacion_id", table, columnPrefix + "_id_habitacion_id"));
        columns.add(Column.aliased("nro_doc_id", table, columnPrefix + "_nro_doc_id"));
        return columns;
    }
}
