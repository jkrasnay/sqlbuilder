package ca.krasnay.sqlbuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    private ParameterizedPreparedStatementCreator ppsc = new ParameterizedPreparedStatementCreator();

    public DeleteCreator(String table) {
        builder = new DeleteBuilder(table);
    }

    public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
        ppsc.setSql(builder.toString());
        return ppsc.createPreparedStatement(conn);
    }

    public DeleteCreator where(String expr) {
        builder.where(expr);
        return this;
    }

    public DeleteCreator whereEquals(String expr, Object value) {

        String param = allocateParameter();

        builder.where(expr + " = :" + param);
        ppsc.setParameter(param, value);

        return this;
    }

}
