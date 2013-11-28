package ca.krasnay.sqlbuilder;

/**
 * SelectBuilder that can be used as a sub-select in a column expression or FROM clause.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class SubSelectBuilder extends SelectBuilder {

    private String alias;

    public SubSelectBuilder(String alias) {
        this.alias = alias;
    }

    protected SubSelectBuilder(SubSelectBuilder other) {
        super(other);
        this.alias = other.alias;
    }

    @Override
    public SubSelectBuilder clone() {
        return new SubSelectBuilder(this);
    }

    @Override
    public String toString() {
        return new StringBuilder()
        .append("(")
        .append(super.toString())
        .append(") as ")
        .append(alias)
        .toString();
    }
}
