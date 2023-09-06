package ca.krasnay.sqlbuilder;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Spring PreparedStatementCreator that you can use like an UpdateBuilder.
 * Example usage is as follows:
 *
 * <pre>
 * PreparedStatementCreator psc = new UpdateCreator(&quot;emp&quot;).setValue(&quot;name&quot;, employee.getName()).whereEquals(&quot;id&quot;,
 *         employeeId);
 *
 * new JdbcTemplate(dataSource).update(psc);
 * </pre>
 *
 * @author John Krasnay <john@krasnay.ca>
 */
public class UpdateCreator extends AbstractSqlCreator {

    private static final long serialVersionUID = 1;

    private UpdateBuilder builder;

    public UpdateCreator(String table) {
        builder = new UpdateBuilder(table);
    }

    @Override
    protected AbstractSqlBuilder getBuilder() {
        return builder;
    }

    public UpdateCreator set(String expr) {
        builder.set(expr);
        return this;
    }

    @Override
    public UpdateCreator setParameter(String name, Object value) {
        super.setParameter(name, value);
        return this;
    }

    public UpdateCreator setValue(String column, Object value) {
        builder.set(column + " = :" + column);
        setParameter(column, value);
        return this;
    }

    public UpdateCreator where(String expr) {
        builder.where(expr);
        return this;
    }

    public UpdateCreator where(Predicate predicate) {
        predicate.init(this);
        builder.where(predicate.toSql());
        return this;
    }

    public UpdateCreator whereEquals(String expr, Object value) {

        String param = allocateParameter();

        builder.where(expr + " = :" + param);
        setParameter(param, value);

        return this;
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

}
