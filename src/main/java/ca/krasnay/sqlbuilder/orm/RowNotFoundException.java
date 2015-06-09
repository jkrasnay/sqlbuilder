package ca.krasnay.sqlbuilder.orm;

import ca.krasnay.sqlbuilder.SelectCreator;

/**
 * Gets thrown when there is no entries in the table identified by specific id
 *
 * @author Alex Rykov
 *
 */
public class RowNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String table;

    private Object id;

    public RowNotFoundException(SelectCreator creator) {
        super("Expected single result, found zero rows for this query: " + creator);
    }

    public RowNotFoundException(String table, Object id) {
        super("Could not find row in table " + table + " with id " + id);
        this.table = table;
        this.id = id;
    }

    public Object getId() {
        return id;
    }

    public String getTable() {
        return table;
    }

}
