package ca.krasnay.sqlbuilder.orm;

import javax.sql.DataSource;

/**
 * Object that returns an {@link IdSource} object for a particular mapping.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface IdSourceFactory {

    /**
     * Returns the {@link IdSource} for the given mapping.
     */
    public IdSource getIdSource(DataSource dataSource, Mapping<?> mapping);

}
