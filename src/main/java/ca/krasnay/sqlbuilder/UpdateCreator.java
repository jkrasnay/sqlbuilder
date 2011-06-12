package ca.krasnay.sqlbuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementCreator;

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
public class UpdateCreator implements PreparedStatementCreator {

    private UpdateBuilder builder;

    private ParameterizedPreparedStatementCreator ppsc = new ParameterizedPreparedStatementCreator();

    public UpdateCreator(String table) {
        builder = new UpdateBuilder(table);
    }

    public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
        ppsc.setSql(builder.toString());
        return ppsc.createPreparedStatement(conn);
    }

    public UpdateCreator set(String expr) {
        builder.set(expr);
        return this;
    }

    public UpdateCreator setParameter(String name, Object value) {
        ppsc.setParameter(name, value);
        return this;
    }

    public UpdateCreator setValue(String column, Object value) {
        builder.set(column + " = :" + column);
        ppsc.setParameter(column, value);
        return this;
    }

    public UpdateCreator where(String expr) {
        builder.where(expr);
        return this;
    }

    public UpdateCreator whereEquals(String column, Object value) {
        builder.where(column + " = :" + column);
        ppsc.setParameter(column, value);
        return this;
    }

}
