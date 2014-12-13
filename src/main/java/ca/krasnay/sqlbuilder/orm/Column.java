package ca.krasnay.sqlbuilder.orm;


/**
 * Maps a column in the database to a corresponding field in a Java class.
 *
 * @author krasnay
 */
public class Column {

    /**
     * Returns the portion of the field name after the last dot, as field names
     * may actually be paths.
     */
    private static String toColumnName(String fieldName) {
        int lastDot = fieldName.indexOf('.');
        if (lastDot > -1) {
            return fieldName.substring(lastDot + 1);
        } else {
            return fieldName;
        }
    }

    private String columnName;

    private String fieldName;

    private Converter<?> converter;

    public Column(String fieldName) {
        this(fieldName, toColumnName(fieldName));
    }

    public Column(String fieldName, String columnName) {
        this(fieldName, columnName, null);
    }


    public Column(String fieldName, Converter<?> converter) {
        this(fieldName, toColumnName(fieldName), converter);
    }

    public Column(String fieldName, String columnName, Converter<?> converter) {
        this.fieldName = fieldName;
        this.columnName = columnName;
        this.converter = converter;
    }

    public String getColumnName() {
        return columnName;
    }

    public Converter<?> getConverter() {
        return converter;
    }

    public String getFieldName() {
        return fieldName;
    }

}
