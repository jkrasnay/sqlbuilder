package ca.krasnay.sqlbuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;

/**
 * A Spring PreparedStatementCreator that you can use like a SelectBuilder.
 * Example usage is as follows:
 *
 * <pre>
 * PreparedStatementCreator psc = new SelectCreator()
 * .column("name")
 * .column("salary")
 * .from("emp")
 * .whereEquals("id", employeeId)
 * .and("salary > :limit")
 * .setParameter("limit", 100000);
 *
 * new JdbcTemplate(dataSource).query(psc, new RowMapper() { ... });
 * </pre>
 *
 * @author John Krasnay <john@krasnay.ca>
 */
public class SelectCreator extends AbstractSqlCreator implements Cloneable {

    private static final long serialVersionUID = 1;

    private SelectBuilder builder = new SelectBuilder();

    public SelectCreator() {
    }

    /**
     * Copy constructor. Used by {@link #clone()}.
     *
     * @param other
     *            SelectCreator being cloned.
     */
    protected SelectCreator(SelectCreator other) {
        super(other);
        this.builder = other.builder.clone();
    }

    public SelectCreator and(String expr) {
        return where(expr);
    }

    public SelectCreator and(Predicate predicate) {
        return where(predicate);
    }

    @Override
    public SelectCreator clone() {
        return new SelectCreator(this);
    }

    public SelectCreator column(String name) {
        builder.column(name);
        return this;
    }

    public SelectCreator column(String name, boolean groupBy) {
        builder.column(name, groupBy);
        return this;
    }

    /**
     * Returns a PreparedStatementCreator that returns a count of the rows that
     * this creator would return.
     *
     * @param dialect
     *            Database dialect.
     */
    public PreparedStatementCreator count(final Dialect dialect) {
        return new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con)
            throws SQLException {
                return getPreparedStatementCreator()
                .setSql(dialect.createCountSelect(builder.toString()))
                .createPreparedStatement(con);
            }
        };
    }

    public SelectCreator distinct() {
        builder.distinct();
        return this;
    }

    public SelectCreator forUpdate() {
        builder.forUpdate();
        return this;
    }

    public SelectCreator from(String table) {
        builder.from(table);
        return this;
    }

    @Override
    protected AbstractSqlBuilder getBuilder() {
        return builder;
    }

    public List<UnionSelectCreator> getUnions() {
        List<UnionSelectCreator> unions = new ArrayList<UnionSelectCreator>();
        for (SelectBuilder unionSB : builder.getUnions()) {
            unions.add(new UnionSelectCreator(this, unionSB));
        }
        return unions;
    }

    public SelectCreator groupBy(String expr) {
        builder.groupBy(expr);
        return this;
    }

    public SelectCreator having(String expr) {
        builder.having(expr);
        return this;
    }

    public SelectCreator join(String join) {
        builder.join(join);
        return this;
    }

    public SelectCreator leftJoin(String join) {
        builder.leftJoin(join);
        return this;
    }

    public SelectCreator noWait() {
        builder.noWait();
        return this;
    }

    public SelectCreator orderBy(String name) {
        builder.orderBy(name);
        return this;
    }

    public SelectCreator orderBy(String name, boolean ascending) {
        builder.orderBy(name, ascending);
        return this;
    }

    /**
     * Returns a PreparedStatementCreator that returns a page of the underlying
     * result set.
     *
     * @param dialect
     *            Database dialect to use.
     * @param limit
     *            Maximum number of rows to return.
     * @param offset
     *            Index of the first row to return.
     */
    public PreparedStatementCreator page(final Dialect dialect, final int limit, final int offset) {
        return new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                return getPreparedStatementCreator()
                .setSql(dialect.createPageSelect(builder.toString(), limit, offset))
                .createPreparedStatement(con);
            }
        };
    }

    @Override
    public SelectCreator setParameter(String name, Object value) {
        super.setParameter(name, value);
        return this;
    }

    public SubSelectCreator subSelectColumn(String alias) {
        SubSelectBuilder subSelectBuilder = new SubSelectBuilder(alias);
        builder.column(subSelectBuilder);
        return new SubSelectCreator(this, subSelectBuilder);
    }

    @Override
    public String toString() {
        ParameterizedPreparedStatementCreator ppsc = getPreparedStatementCreator();
        StringBuilder sb = new StringBuilder(builder.toString());
        List<String> params = new ArrayList<String>(ppsc.getParameterMap().keySet());
        Collections.sort(params);
        for (String s : params) {
            sb.append(", ").append(s).append("=").append(ppsc.getParameterMap().get(s));
        }
        return sb.toString();
    }

    public UnionSelectCreator union() {
        SelectBuilder unionSelectBuilder = new SelectBuilder();
        builder.union(unionSelectBuilder);
        return new UnionSelectCreator(this, unionSelectBuilder);
    }

    public SelectCreator where(String expr) {
        builder.where(expr);
        return this;
    }

    public SelectCreator where(Predicate predicate) {
        predicate.init(this);
        builder.where(predicate.toSql());
        return this;
    }

    public SelectCreator whereEquals(String expr, Object value) {

        String param = allocateParameter();

        builder.where(expr + " = :" + param);
        setParameter(param, value);

        return this;
    }

    public SelectCreator whereIn(String expr, List<?> values) {

        StringBuilder sb = new StringBuilder();
        sb.append(expr).append(" in (");

        boolean first = true;
        for (Object value : values) {
            String param = allocateParameter();
            setParameter(param, value);
            if (!first) {
                sb.append(", ");
            }
            sb.append(":").append(param);
            first = false;
        }

        sb.append(")");
        builder.where(sb.toString());

        return this;
    }

    public SelectCreator with(String hint) {
        builder.with(hint);
        return this;
    }
}
