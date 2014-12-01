package ca.krasnay.sqlbuilder.orm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Converts java.util.Date and java.sql.Timestamp to java.sql.Timestamp
 *
 * @author Alex Rykov
 *
 */
public final class TimestampConverter implements Converter<Date> {

    private static final TimestampConverter INSTANCE = new TimestampConverter();

    public static TimestampConverter getInstance() {
        return INSTANCE;
    }

    private TimestampConverter() {

    }

    @Override
    public Object convertFieldValueToColumn(Date fieldValue) {
        if (fieldValue == null || fieldValue instanceof Timestamp) {
            return fieldValue;
        } else if (fieldValue instanceof Date) {
            return new Timestamp((fieldValue).getTime());
        } else {
            throw new RuntimeException("Unsupported class: " + fieldValue.getClass());
        }
    }

    @Override
    public Date getFieldValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
        return rs.getTimestamp(columnLabel);
    }

}
