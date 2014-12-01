package ca.krasnay.sqlbuilder.orm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.joda.time.DateTime;

public final class DateTimeConverter implements Converter<DateTime> {

    private static final DateTimeConverter INSTANCE = new DateTimeConverter();

    public static DateTimeConverter getInstance() {
        return INSTANCE;
    }

    // TODO make me private as soon as Mapping.addFields becomes widespread
    public DateTimeConverter() {
    }

    @Override
    public Object convertFieldValueToColumn(DateTime fieldValue) {
        return fieldValue != null ? new java.sql.Timestamp(fieldValue.getMillis()) : null;
    }

    @Override
    public DateTime getFieldValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {

        Timestamp timestamp = rs.getTimestamp(columnLabel);

        if (timestamp == null) {
            return null;
        } else {
            return new DateTime(timestamp);
        }

    }

}
