package ca.krasnay.sqlbuilder.orm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converter for a list of strings that stores the list in the database as
 * a comma-separated list. Commas in values are escaped with a backslash
 * character. Upon load, the resulting list is an ArrayList&lt;String&gt;
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class StringMapConverter implements Converter<Map<String, String>> {

    private static final String KEY_VALUE_SEPARATOR = "=";

    private StringListFlattener flattener;

    public StringMapConverter() {
        flattener = new StringListFlattener();
    }

    @Override
    public Object convertFieldValueToColumn(Map<String, String> fieldValue) {

        List<String> list = new ArrayList<String>();

        for (String key : fieldValue.keySet()) {
            if (key.indexOf(KEY_VALUE_SEPARATOR) >= 0) {
                throw new IllegalArgumentException(
                        String.format("Illegal key '%s', must not contain '%s'", key, KEY_VALUE_SEPARATOR));
            }
            list.add(key + KEY_VALUE_SEPARATOR + fieldValue.get(key));
        }

        return flattener.join(list);
    }

    @Override
    public Map<String, String> getFieldValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {

        Map<String, String> result = new HashMap<String, String>();

        String stringValue = rs.getString(columnLabel);

        if (stringValue != null) {
            List<String> list = flattener.split(stringValue);
            for (String s : list) {
                int i = s.indexOf(KEY_VALUE_SEPARATOR);
                if (i < 0) {
                    throw new IllegalArgumentException(
                            String.format("Invalid key value pair: '%s'", s));
                }
                result.put(s.substring(0, i), s.substring(i+1));
            }
        }

        return result;

    }

}
