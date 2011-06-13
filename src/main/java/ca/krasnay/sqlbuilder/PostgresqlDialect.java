package ca.krasnay.sqlbuilder;

/**
 * Dialect for PostgreSQL.
 *
 * @author John Krasnay <john@krasnay.ca>
 */
public class PostgresqlDialect implements Dialect {

    public String createCountSelect(String sql) {
        return "select count(*) from (" + sql + ") a";
    }

    public String createPageSelect(String sql, int limit, int offset) {
        return String.format("%s limit %d offset %d", sql, limit, offset);
    }

}
