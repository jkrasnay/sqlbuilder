package ca.krasnay.sqlbuilder;

import java.io.Serializable;


/**
 * Creator for part of a SQL select statement that comes after the UNION keyword.
 * You shouldn't create these directly. Instead, acquire one from the {@link SelectCreator#union()} method.
 *
 * @author John Krasnay <john@krasnay.ca>
 */
public class UnionSelectCreator implements Serializable {

    private static final long serialVersionUID = 1;

    /**
     * Builder that builds this select.
     */
    private SelectBuilder builder;

    /**
     * Owning select creator. Parameters are stored here.
     */
    private SelectCreator creator;

    UnionSelectCreator(SelectCreator creator, SelectBuilder builder) {
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
    protected UnionSelectCreator(SelectCreator owner, UnionSelectCreator other) {
        this.builder = other.builder.clone();
        this.creator = owner;
    }

    public UnionSelectCreator clone(SelectCreator owner) {
        return new UnionSelectCreator(owner, this);
    }

    public UnionSelectCreator and(String expr) {
        builder.and(expr);
        return this;
    }

    public UnionSelectCreator column(String name) {
        builder.column(name);
        return this;
    }

    public UnionSelectCreator column(String name, boolean groupBy) {
        builder.column(name, groupBy);
        return this;
    }

    public UnionSelectCreator distinct() {
        builder.distinct();
        return this;
    }

    public UnionSelectCreator from(String table) {
        builder.from(table);
        return this;
    }

    public UnionSelectCreator groupBy(String expr) {
        builder.groupBy(expr);
        return this;
    }

    public UnionSelectCreator having(String expr) {
        builder.having(expr);
        return this;
    }

    public UnionSelectCreator join(String join) {
        builder.join(join);
        return this;
    }

    public UnionSelectCreator leftJoin(String join) {
        builder.leftJoin(join);
        return this;
    }

    public UnionSelectCreator setParameter(String name, Object value) {
        creator.setParameter(name, value);
        return this;
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    public UnionSelectCreator where(String expr) {
        builder.where(expr);
        return this;
    }

    public UnionSelectCreator whereEquals(String expr, Object value) {

        String param = creator.allocateParameter();

        builder.where(expr + " = :" + param);
        creator.setParameter(param, value);

        return this;
    }
}
