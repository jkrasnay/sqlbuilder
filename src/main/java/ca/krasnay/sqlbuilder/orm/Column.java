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

    private String columnExpr;

    private String columnName;

    private String fieldName;

    private Converter<?> converter;

    public Column(String fieldName) {
        this(fieldName, toColumnName(fieldName));
    }

    public Column(String fieldName, String columnName) {
        this(fieldName, columnName, (Converter<?>) null);
    }

    /**
     * Constructor for a read-only column. Read-only columns are used only in
     * queries and are ignored for inserts and updates, and may be populated
     * using a SQL query.
     *
     * <p>Use read-only columns where the database column is calculated using
     * a trigger, or where you want to set a field in your Java object based
     * on a SQL expression.
     *
     * <p>Examples:
     *
     * <pre>
     * mapping.addColumn(new Column("size", "length(content)", "size");
     * mapping.addColumn(new Column("children", "(select count(*) from Child c where c.parentId = p.id)", "children");
     *
     * // "used" here is calculated dynamically in a trigger
     * mapping.addColumn(new Column("used", "used", "used"));
     * </pre>
     *
     * @param fieldName
     *            Name of the field in the Java class. If the field is in an
     *            embedded object, this should be a dot-separated path, e.g.
     *            <code>address.city</code>.
     * @param columnExpr
     *            SQL expression that produces the value for the field.
     * @param columnName
     *            Column name used as an alias.
     */
    public Column(String fieldName, String columnExpr, String columnName) {
        this.fieldName = fieldName;
        this.columnExpr = columnExpr;
        this.columnName = columnName;
    }

    public Column(String fieldName, Converter<?> converter) {
        this(fieldName, toColumnName(fieldName), converter);
    }

    public Column(String fieldName, String columnName, Converter<?> converter) {
        this.fieldName = fieldName;
        this.columnName = columnName;
        this.converter = converter;
    }

    public String getColumnExpr() {
        return columnExpr;
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

    public boolean isReadOnly() {
        return columnExpr != null;
    }
}
