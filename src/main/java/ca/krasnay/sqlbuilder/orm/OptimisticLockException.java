package ca.krasnay.sqlbuilder.orm;

/**
 * Gets thrown when record was updated by somebody else in the time span
 * between reading entity and updating it. Only applies to VersionedEntity
 *
 * @author Alex Rykov
 *
 */
public class OptimisticLockException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String table;

    private Object id;

    public OptimisticLockException(String table, Object id) {
        super("Could not update row in table " + table + " with id " + id);
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
