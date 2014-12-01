package ca.krasnay.sqlbuilder.orm;


/**
 * Maps a column in the database to a corresponding field in a Java class.
 *
 * @author krasnay
 */
public class Column {

    private String columnName;

    private String fieldName;

    private Converter<?> converter;

    public Column(String name) {
        this(name, name);
    }

    public Column(String fieldName, String columnName) {
        this(fieldName, columnName, null);
    }


    public Column(String fieldName, Converter<?> converter) {
        this(fieldName, fieldName, converter);
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
