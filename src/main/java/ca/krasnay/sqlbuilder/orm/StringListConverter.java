package ca.krasnay.sqlbuilder.orm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Converter for a list of strings that stores the list in the database as
 * a comma-separated list. Commas in values are escaped with a backslash
 * character. Upon load, the resulting list is an ArrayList&lt;String&gt;
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class StringListConverter implements Converter<List<String>> {

    private StringListFlattener flattener;

    public StringListConverter() {
        flattener = new StringListFlattener();
    }

    @Override
    public Object convertFieldValueToColumn(List<String> fieldValue) {
        return flattener.join(fieldValue);
    }

    @Override
    public List<String> getFieldValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
        return flattener.split(rs.getString(columnLabel));
    }

}
