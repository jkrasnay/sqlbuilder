package ca.krasnay.sqlbuilder;

/**
 * Component in the where clause of a {@link SelectCreator}, {@link UpdateCreator}, or {@link DeleteCreator}.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface Predicate {

    /**
     * Initializes the predicate. For example, this may allocate one or more
     * parameters from the creator and set values for the parameters. This is
     * called by the creator when the predicate is added to it.
     */
    public void init(AbstractSqlCreator creator);

    /**
     * Returns an SQL expression representing the predicate. Parameters may be
     * included preceded by a colon.
     */
    public String toSql();

}
