package ca.krasnay.sqlbuilder;


/**
 * A Spring PreparedStatementCreator that you can use like a DeleteBuilder.
 * Example usage is as follows:
 *
 * <pre>
 * PreparedStatementCreator psc = new DeleteCreator(&quot;emp&quot;).whereEquals(&quot;id&quot;,
 *         employeeId);
 *
 * new JdbcTemplate(dataSource).update(psc);
 * </pre>
 *
 * @author John Krasnay <john@krasnay.ca>
 */
public class DeleteCreator extends AbstractSqlCreator {

    private static final long serialVersionUID = 1;

    private DeleteBuilder builder;

    public DeleteCreator(String table) {
        builder = new DeleteBuilder(table);
    }

    @Override
    protected AbstractSqlBuilder getBuilder() {
        return builder;
    }

    public DeleteCreator where(String expr) {
        builder.where(expr);
        return this;
    }

    public DeleteCreator where(Predicate predicate) {
        predicate.init(this);
        builder.where(predicate.toSql());
        return this;
    }

    public DeleteCreator whereEquals(String expr, Object value) {

        String param = allocateParameter();

        builder.where(expr + " = :" + param);
        setParameter(param, value);

        return this;
    }

}
