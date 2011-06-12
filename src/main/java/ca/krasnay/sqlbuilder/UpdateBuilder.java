package ca.krasnay.sqlbuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for creating SQL update statements.
 *
 * @author John Krasnay <john@krasnay.ca>
 */
public class UpdateBuilder extends AbstractSqlBuilder {

    private String table;

    private List<String> sets = new ArrayList<String>();

    private List<String> wheres = new ArrayList<String>();

    public UpdateBuilder(String table) {
        this.table = table;
    }

    public UpdateBuilder set(String expr) {
        sets.add(expr);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder("update ").append(table);
        appendList(sql, sets, " set ", ", ");
        appendList(sql, wheres, " where ", " and ");
        return sql.toString();
    }

    public UpdateBuilder where(String expr) {
        wheres.add(expr);
        return this;
    }

}
