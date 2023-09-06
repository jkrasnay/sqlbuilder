package ca.krasnay.sqlbuilder;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;

/**
 * A Spring PreparedStatementCreator that you can use like an InsertBuilder.
 * Example usage is as follows:
 *
 * <pre>
 * PreparedStatementCreator psc = new InsertCreator(&quot;emp&quot;).setRaw(&quot;id&quot;, &quot;emp_id_seq.nextval&quot;).setValue(&quot;name&quot;,
 *         employee.getName());
 *
 * new JdbcTemplate(dataSource).update(psc);
 * </pre>
 *
 * @author John Krasnay <john@krasnay.ca>
 */
public class InsertCreator implements PreparedStatementCreator, Serializable {

    private static final long serialVersionUID = 1;

    private InsertBuilder builder;

    private ParameterizedPreparedStatementCreator ppsc = new ParameterizedPreparedStatementCreator();

    public InsertCreator(String table) {
        builder = new InsertBuilder(table);
    }

    public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
        ppsc.setSql(builder.toString());
        return ppsc.createPreparedStatement(conn);
    }

    public ParameterizedPreparedStatementCreator setParameter(String name, Object value) {
        return ppsc.setParameter(name, value);
    }

    public InsertCreator setRaw(String column, String value) {
        builder.set(column, value);
        return this;
    }

    public InsertCreator setValue(String column, Object value) {
        setRaw(column, ":" + column);
        setParameter(column, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(builder.toString());
        List<String> params = new ArrayList<String>(ppsc.getParameterMap().keySet());
        Collections.sort(params);
        for (String s : params) {
            sb.append(", ").append(s).append("=").append(ppsc.getParameterMap().get(s));
        }
        return sb.toString();
    }
}
