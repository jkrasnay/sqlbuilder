package ca.krasnay.sqlbuilder.orm;

/**
 * Interface to generate unique IDs for primary keys.
 */
public interface IdSource {

    /**
     * Returns the next available ID.
     */
    public Object nextId();

}
