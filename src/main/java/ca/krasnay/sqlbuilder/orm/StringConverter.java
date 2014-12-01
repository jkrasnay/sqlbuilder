package ca.krasnay.sqlbuilder.orm;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Converts to a string for database storage. Forces the use of
 * {@link ResultSet#getString(String)} instead of the default
 * {@link ResultSet#getObject(String)}. This is mostly useful for CLOB columns
 * in Oracle, which return a CLOB object rather than a string when getObject()
 * is called.
 *
 * @author John Krasnay <john@krasnay.ca>
 *
 */
public final class StringConverter implements Converter<String> {

    private static final StringConverter INSTANCE = new StringConverter();

    private StringConverter() {
    }

    public static StringConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public Object convertFieldValueToColumn(String fieldValue) {
        return fieldValue == null ? null : fieldValue.toString();
    }

    @Override
    public String getFieldValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
        return rs.getString(columnLabel);
    }

}
