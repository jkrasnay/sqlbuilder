package ca.krasnay.sqlbuilder.orm;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface that defines conversions of a domain value to and from a field in
 * ResultSet.
 */
public interface Converter<T> {

    /**
     * Converts a Java value into the corresponding value for the database.
     *
     * @param fieldValue
     *            Java value to be converted. May be null.
     */
    public Object convertFieldValueToColumn(T fieldValue);

    /**
     * Returns a Java value from a result set.
     *
     * @param rs
     *            Result set from which to get the value.
     * @param columnLabel
     *            Label of the column with which to access the result set.
     */
    public T getFieldValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException;

}
