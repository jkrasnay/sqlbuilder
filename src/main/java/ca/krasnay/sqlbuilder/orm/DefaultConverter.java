package ca.krasnay.sqlbuilder.orm;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Converter that takes objects as is and does not perform any conversions beyond JDBC
 *
 * @author Alex Rykov
 *
 */
public class DefaultConverter implements Converter<Object> {

    private static final DefaultConverter INSTANCE = new DefaultConverter();

    public static DefaultConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public Object convertFieldValueToColumn(Object fieldValue) {
        return fieldValue;
    }

    @Override
    public Object getFieldValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
        Object value = rs.getObject(columnLabel);
        if (value instanceof Clob) {
            Clob clob = (Clob) value;
            return clob.getSubString(1, (int) clob.length());
        } else {
            return value;
        }
    }

}
