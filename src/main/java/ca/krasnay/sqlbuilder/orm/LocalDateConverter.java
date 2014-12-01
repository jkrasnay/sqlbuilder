package ca.krasnay.sqlbuilder.orm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public final class LocalDateConverter implements Converter<LocalDate> {

    private static final LocalDateConverter INSTANCE = new LocalDateConverter();

    public static LocalDateConverter getInstance() {
        return INSTANCE;
    }

    // TODO make me private as soon as Mapping.addFields becomes widespread
    public LocalDateConverter() {
    }

    @Override
    public Object convertFieldValueToColumn(LocalDate fieldValue) {
        return fieldValue != null ? new java.sql.Date(fieldValue.toDateMidnight().getMillis()) : null;
    }

    @Override
    public LocalDate getFieldValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {

        Timestamp timestamp = rs.getTimestamp(columnLabel);

        if (timestamp == null) {
            return null;
        } else {
            return new DateTime(timestamp).toLocalDate();
        }

    }

}
