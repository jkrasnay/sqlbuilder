package ca.krasnay.sqlbuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;

/**
 * Implementation of Spring's PreparedStatementCreator that allows for the
 * substitution of named parameters. Here's a simple example:
 *
 * <pre>
 * PreparedStatement ps = new ParameterizedPreparedStatementCreator().setSql(
 *         &quot;select * from Employee where name like :name&quot;).setParameter(&quot;name&quot;, &quot;Bob%&quot;).createPreparedStatement(conn);
 * </pre>
 *
 * The implementation performs simple text substitution of parameters, which can
 * lead to problems with things like this:
 * "select * from Employee where name = 'foo:bar'" In this case,
 * {@link #createPreparedStatement(Connection)} will fail if the
 * <code>bar</code> is not defined, or will result in
 * "select * from Employee where name = 'foo?'", both of which are wrong.
 *
 * @author John Krasnay <john@krasnay.ca>
 *
 */
public class ParameterizedPreparedStatementCreator implements PreparedStatementCreator {

    static class SqlAndParams {

        private String sql;
        private List<Object> params;

        private SqlAndParams(String sql, List<Object> params) {
            super();
            this.sql = sql;
            this.params = params;
        }

        public List<Object> getParams() {
            return params;
        }

        public String getSql() {
            return sql;
        }
    }

    private static final Logger log = LoggerFactory.getLogger(ParameterizedPreparedStatementCreator.class);

    private static final String NAME_REGEX = "[a-z][_a-z0-9]*";

    private static final String PARAM_REGEX = ":(" + NAME_REGEX + ")";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX, Pattern.CASE_INSENSITIVE);

    private static final Pattern PARAM_PATTERN = Pattern.compile(PARAM_REGEX, Pattern.CASE_INSENSITIVE);

    private String sql;

    private Map<String, Object> parameterMap = new HashMap<String, Object>();

    public ParameterizedPreparedStatementCreator() {
    }

    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

        log.debug("createPreparedStatement: %s", sql);
        for (String key : parameterMap.keySet()) {
            log.debug("    %s => %s", key, parameterMap.get(key));
        }

        SqlAndParams sap = createSqlAndParams();

        PreparedStatement ps = con.prepareStatement(sap.getSql());

        for (int i = 0; i < sap.getParams().size(); i++) {
            ps.setObject(i + 1, sap.getParams().get(i));
        }

        return ps;
    }

    SqlAndParams createSqlAndParams() {

        //
        // Replace all parameters with question marks, and build a list of
        // parameter
        // values in the same order.
        //

        StringBuilder psSql = new StringBuilder();
        List<Object> paramValues = new ArrayList<Object>();

        Matcher m = PARAM_PATTERN.matcher(sql);
        int index = 0;
        while (m.find(index)) {
            psSql.append(sql.substring(index, m.start()));
            String name = m.group(1);
            index = m.end();
            if (parameterMap.containsKey(name)) {
                psSql.append("?");
                paramValues.add(parameterMap.get(name));
            } else {
                throw new IllegalArgumentException("Unknown parameter '" + name + "' at position " + m.start());
            }
        }

        // Any stragglers?
        psSql.append(sql.substring(index));

        return new SqlAndParams(psSql.toString(), paramValues);
    }

    public Map<String, Object> getParameterMap() {
        return Collections.unmodifiableMap(parameterMap);
    }

    public String getSql() {
        return sql;
    }

    public ParameterizedPreparedStatementCreator setParameter(String name, Object value) {

        if (NAME_PATTERN.matcher(name).matches()) {
            parameterMap.put(name, value);
        } else {
            throw new IllegalArgumentException(
                    "'"
                            + name
                            + "' is not a valid parameter name. Names must start with a letter, and contain only letters, numbers, and underscores.");
        }

        return this;
    }

    public ParameterizedPreparedStatementCreator setSql(String sql) {
        this.sql = sql;
        return this;
    }

}
