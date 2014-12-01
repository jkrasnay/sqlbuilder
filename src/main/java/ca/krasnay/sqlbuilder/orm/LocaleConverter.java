package ca.krasnay.sqlbuilder.orm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Converts between a Locale object and a string.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class LocaleConverter implements Converter<Locale> {

    private static LocaleConverter INSTANCE = new LocaleConverter();

    public static LocaleConverter getInstance() {
        return INSTANCE;
    }

    private LocaleConverter() {

    }

    @Override
    public Object convertFieldValueToColumn(Locale fieldValue) {
        return fieldValue != null ? fieldValue.toString() : null;
    }

    @Override
    public Locale getFieldValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {

        String s = rs.getString(columnLabel);

        if (s == null) {
            return null;
        } else {
            String parts[] = s.split("_", 3);
            if (parts.length == 1) {
                return new Locale(parts[0]);
            } else if (parts.length == 2) {
                return new Locale(parts[0], parts[1]);
            } else {
                return new Locale(parts[0], parts[1], parts[2]);
            }
        }

    }

}
