package ca.krasnay.sqlbuilder.orm;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ClassConverter implements Converter<Class<?>> {

    private static final ClassConverter INSTANCE = new ClassConverter();

    public static ClassConverter getInstance() {
        return INSTANCE;
    }

    // TODO make me private as soon as Mapping.addFields becomes widespread
    public ClassConverter() {
    }

    @Override
    public Object convertFieldValueToColumn(Class<?> fieldValue) {
        return fieldValue != null ? fieldValue.getName() : null;
    }

    @Override
    public Class<?> getFieldValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {

        String className = rs.getString(columnLabel);

        if (className == null) {
            return null;
        } else {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
