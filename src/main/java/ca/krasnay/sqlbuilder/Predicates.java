package ca.krasnay.sqlbuilder;

import java.util.Arrays;
import java.util.List;

/**
 * Collection of commonly used predicates, implemented by static methods.
 *
 * <p>
 * Most predicates accept a SQL expression and one or more values to which the
 * SQL is compared. Predicates do not escape this expression. As such, do not
 * accept arbitrary expressions from users or other sources, as it may be a
 * source of SQL injection vulnerabilities. The normal use-case is that these
 * expressions are hard-coded in your application. Values, on the other hand,
 * are substituted as proper prepared statement parameters so they are safe
 * from SQL injection.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public final class Predicates {

    /**
     * Adds an equals clause to a creator.
     *
     * @param expr
     *            SQL expression to be compared for equality.
     * @param value
     *            Value to which the SQL expression is compared.
     */
    public static Predicate eq(final String expr, final Object value) {
        return new Predicate() {
            private String param;
            public void init(AbstractSqlCreator creator) {
                param = creator.allocateParameter();
                creator.setParameter(param, value);
            }
            public String toSql() {
                return String.format("%s = :%s", expr, param);
            }
        };
    }


    /**
     * Adds an IN clause to a creator.
     *
     * @param expr
     *            SQL expression to be tested for inclusion.
     * @param values
     *            Values for the IN clause.
     */
    public static Predicate in(final String expr, final List<?> values) {

        return new Predicate() {

            private String sql;

            public void init(AbstractSqlCreator creator) {

                StringBuilder sb = new StringBuilder();
                sb.append(expr).append(" in (");

                boolean first = true;
                for (Object value : values) {
                    String param = creator.allocateParameter();
                    creator.setParameter(param, value);
                    if (!first) {
                        sb.append(", ");
                    }
                    sb.append(":").append(param);
                    first = false;
                }

                sb.append(")");

                sql = sb.toString();

            }

            public String toSql() {
                return sql;
            }
        };
    }


    /**
     * Adds an IN clause to a creator.
     *
     * @param expr
     *            SQL expression to be tested for inclusion.
     * @param values
     *            Values for the IN clause.
     */
    public static Predicate in(final String expr, final Object... values) {
        return in(expr, Arrays.asList(values));
    }

    /**
     * Adds an EXISTS clause to a creator. Typical usage is as follows:
     *
     * <pre>
     * new SelectCreator()
     * .column("name")
     * .from("Emp e")
     * .where(exists("SickDay sd").where("sd.emp_id = e.id").and(eq("sd.dow", "Monday")));
     * </pre>
     *
     * @param table
     *            Table that forms the basis of the sub-select.
     */
    public static ExistsPredicate exists(String table) {
        return new ExistsPredicateImpl(table);
    }

    /**
     * Inverts the sense of the given child predicate. In SQL terms, this
     * surrounds the given predicate with "not (...)".
     *
     * @param childPredicate
     *            Predicate whose sense is to be inverted.
     */
    public static Predicate not(final Predicate childPredicate) {
        return new Predicate() {
            public void init(AbstractSqlCreator creator) {
                childPredicate.init(creator);
            }
            public String toSql() {
                return "not (" + childPredicate.toSql() + ")";
            }
        };
    }
}
