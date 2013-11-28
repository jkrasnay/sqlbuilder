package ca.krasnay.sqlbuilder;

import java.io.Serializable;


/**
 * Creator for part of a SQL sub-select statement used as a column expression or a FROM clause.
 * You shouldn't create these directly. Instead, acquire one from the {@link SelectCreator#subSelectColumn(String)} method.
 *
 * @author John Krasnay <john@krasnay.ca>
 */
public class SubSelectCreator implements Serializable {

    private static final long serialVersionUID = 1;

    /**
     * Builder that builds this select.
     */
    private SubSelectBuilder builder;

    /**
     * Owning select creator. Parameters are stored here.
     */
    private SelectCreator creator;

    SubSelectCreator(SelectCreator creator, SubSelectBuilder builder) {
        this.builder = builder;
        this.creator = creator;
    }

    /**
     * Copy constructor. Used by {@link #clone()}.
     *
     * @param owner
     *            SelectCreator that owns the new UnionSelectCreator
     * @param other
     *            UnionSelectCreator being cloned.
     */
    protected SubSelectCreator(SelectCreator owner, SubSelectCreator other) {
        this.builder = other.builder.clone();
        this.creator = owner;
    }

    public SubSelectCreator and(Predicate predicate) {
        predicate.init(creator);
        builder.where(predicate.toSql());
        return this;
    }

    public SubSelectCreator and(String expr) {
        builder.and(expr);
        return this;
    }

    public SubSelectCreator clone(SelectCreator owner) {
        return new SubSelectCreator(owner, this);
    }

    public SubSelectCreator column(String name) {
        builder.column(name);
        return this;
    }

    public SubSelectCreator column(String name, boolean groupBy) {
        builder.column(name, groupBy);
        return this;
    }

    public SubSelectCreator distinct() {
        builder.distinct();
        return this;
    }

    public SubSelectCreator from(String table) {
        builder.from(table);
        return this;
    }

    public SubSelectCreator groupBy(String expr) {
        builder.groupBy(expr);
        return this;
    }

    public SubSelectCreator having(String expr) {
        builder.having(expr);
        return this;
    }

    public SubSelectCreator join(String join) {
        builder.join(join);
        return this;
    }

    public SubSelectCreator leftJoin(String join) {
        builder.leftJoin(join);
        return this;
    }

    public SubSelectCreator setParameter(String name, Object value) {
        creator.setParameter(name, value);
        return this;
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    public SubSelectCreator where(Predicate predicate) {
        predicate.init(creator);
        builder.where(predicate.toSql());
        return this;
    }

    public SubSelectCreator where(String expr) {
        builder.where(expr);
        return this;
    }

}
